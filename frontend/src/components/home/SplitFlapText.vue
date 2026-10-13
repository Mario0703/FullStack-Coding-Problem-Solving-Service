<script setup lang="ts">
import {
  computed,
  onMounted,
  onUnmounted,
  ref,
  watch,
  type CSSProperties,
  type ComponentPublicInstance,
} from 'vue'

interface TileState {
  current: string
  next: string
  flipping: boolean
  tick: number
}

interface AnimationPlan {
  index: number
  from: string
  target: string
  sequence: string[]
  start: number
  step: number
  done: boolean
}

interface TileUpdate {
  index: number
  current: string
  next: string
  done: boolean
}

interface SplitFlapTextProps {
  words?: string[]
  text?: string
  flipDuration?: number
  stagger?: number
  cycleDelay?: number
  charset?: string
  flipsPerChar?: number
  tileColor?: string
  textColor?: string
  tileRadius?: number | string
  gap?: number | string
  fontSize?: number | string
  loop?: boolean
  padTo?: number
  className?: string
  style?: CSSProperties
}

const ALPHANUMERIC = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'

const CHARSETS: Record<string, string> = {
  alpha: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
  alphanumeric: ALPHANUMERIC,
  numeric: '0123456789',
}

const FLIP_EASING = 'cubic-bezier(0.23, 1, 0.32, 1)'

const toCssUnit = (value: number | string): string =>
  typeof value === 'number' ? `${value}px` : value

const resolveCharset = (charset: string): string => {
  const preset = CHARSETS[charset]
  if (preset) return preset
  return charset.length > 0 ? charset : ALPHANUMERIC
}

const normalizePhrase = (phrase: string, width: number): string => {
  const safe = String(phrase ?? '')
  return safe.padEnd(width, ' ').slice(0, width)
}

const createTiles = (phrase: string): TileState[] =>
  phrase.split('').map((char) => ({
    current: char,
    next: char,
    flipping: false,
    tick: 0,
  }))

const sampleChar = (charset: string): string =>
  charset.charAt(Math.floor(Math.random() * charset.length)) || ' '

const buildSequence = (target: string, flips: number, charset: string): string[] => {
  const steps: string[] = []
  for (let i = 0; i < flips; i += 1) {
    steps.push(sampleChar(charset))
  }
  steps.push(target)
  return steps
}

const props = withDefaults(defineProps<SplitFlapTextProps>(), {
  words: () => ['LAUNCH READY', 'SYNC ONLINE', 'SIGNAL LIVE'],
  flipDuration: 0.12,
  stagger: 0.06,
  cycleDelay: 2400,
  charset: 'alphanumeric',
  flipsPerChar: 8,
  tileColor: '#1b2711',
  textColor: '#f8fafc',
  tileRadius: 8,
  gap: 6,
  fontSize: 52,
  loop: true,
  padTo: 12,
  className: '',
})

// Tailwind utilities from the original snippet are expressed as scoped classes
// (see <style scoped> below) because this project does not ship Tailwind.
const rootClass = 'split-flap'
const tileClass = 'split-flap__tile'

const halfTopClass = 'split-flap__half split-flap__half--top'
const halfBottomClass = 'split-flap__half split-flap__half--bottom'

const flapFrontClass = 'split-flap__flap split-flap__flap--front'
const flapBackClass = 'split-flap__flap split-flap__flap--back'

const charTopClass = 'split-flap__char split-flap__char--top'
const charBottomClass = 'split-flap__char split-flap__char--bottom'

const sourceWords = computed(() => props.words)
const phrasesKey = computed(() =>
  typeof props.text === 'string'
    ? props.text
    : sourceWords.value.map((word) => String(word ?? '')).join('\u001f'),
)
const phrases = computed(() => phrasesKey.value.split('\u001f'))
const width = computed(() => {
  const longest = phrases.value.reduce((max, phrase) => Math.max(max, phrase.length), 1)
  return Math.max(1, Math.ceil(Number(props.padTo) || 0), longest)
})
const normalizedPhrases = computed(() =>
  phrases.value.map((phrase) => normalizePhrase(phrase, width.value)),
)

const tiles = ref<TileState[]>(createTiles(normalizedPhrases.value[0] || ''))
const settledText = computed(() =>
  tiles.value
    .map((tile) => tile.current)
    .join('')
    .trimEnd(),
)

const componentStyle = computed<CSSProperties>(
  () =>
    ({
      '--split-flap-tile-color': props.tileColor,
      '--split-flap-text-color': props.textColor,
      '--split-flap-radius': toCssUnit(props.tileRadius),
      '--split-flap-gap': toCssUnit(props.gap),
      '--split-flap-font-size': toCssUnit(props.fontSize),
      '--split-flap-flip-duration': `${Math.max(0.04, Number(props.flipDuration) || 0.12)}s`,
      ...props.style,
    }) as CSSProperties,
)

const flipDurationMs = computed(() => Math.max(0.04, Number(props.flipDuration) || 0.12) * 1000)

const playFrontFlip = (el: Element | ComponentPublicInstance | null) => {
  if (!(el instanceof HTMLElement)) return
  el.animate(
    [
      { transform: 'rotateX(0deg)', filter: 'brightness(1.08)' },
      { transform: 'rotateX(-90deg)', filter: 'brightness(0.52)' },
    ],
    { duration: flipDurationMs.value, easing: FLIP_EASING, fill: 'both' },
  )
}

const playBackFlip = (el: Element | ComponentPublicInstance | null) => {
  if (!(el instanceof HTMLElement)) return
  el.animate(
    [
      { transform: 'rotateX(90deg)', filter: 'brightness(0.58)', offset: 0 },
      { transform: 'rotateX(90deg)', filter: 'brightness(0.58)', offset: 0.45 },
      { transform: 'rotateX(0deg)', filter: 'brightness(1)', offset: 1 },
    ],
    { duration: flipDurationMs.value, easing: FLIP_EASING, fill: 'both' },
  )
}

const prefersReducedMotion = ref(false)
let mediaQuery: MediaQueryList | null = null
let handleMediaChange: (() => void) | null = null

onMounted(() => {
  if (typeof window === 'undefined' || !window.matchMedia) return
  mediaQuery = window.matchMedia('(prefers-reduced-motion: reduce)')
  handleMediaChange = () => {
    prefersReducedMotion.value = mediaQuery!.matches
  }
  handleMediaChange()
  mediaQuery.addEventListener('change', handleMediaChange)
})

onUnmounted(() => {
  if (mediaQuery && handleMediaChange) mediaQuery.removeEventListener('change', handleMediaChange)
})

let raf: number | null = null
let cycleTimer: number | null = null
let currentText = ''
let teardownCycle: (() => void) | null = null

const clearAnimation = () => {
  if (raf) {
    cancelAnimationFrame(raf)
    raf = null
  }
  if (cycleTimer) {
    clearTimeout(cycleTimer)
    cycleTimer = null
  }
}

const setupCycle = () => {
  clearAnimation()

  const firstPhrase = normalizedPhrases.value[0] || ''

  // Always start from a blank board so the first phrase flips into view.
  // Without this, a single-phrase display (e.g. the landing page title) would
  // render statically because there is nothing to cycle to yet.
  currentText = ' '.repeat(width.value)
  tiles.value = createTiles(currentText)

  if (typeof window === 'undefined') {
    currentText = firstPhrase
    tiles.value = createTiles(firstPhrase)
    return
  }

  let phraseIndex = 0
  let cancelled = false

  const safeFlipMs = Math.max(40, (Number(props.flipDuration) || 0.12) * 1000)
  const safeStaggerMs = Math.max(0, (Number(props.stagger) || 0) * 1000)
  const safeCycleDelay = Math.max(400, Number(props.cycleDelay) || 2400)
  const safeFlips = Math.max(0, Math.floor(Number(props.flipsPerChar) || 0))
  const activeCharset = resolveCharset(props.charset)

  const animateTo = (targetPhrase: string): number => {
    if (prefersReducedMotion.value) {
      currentText = targetPhrase
      tiles.value = createTiles(targetPhrase)
      return 0
    }

    const fromPhrase = normalizePhrase(currentText, width.value)
    const targetChars = targetPhrase.split('')

    const plans = targetChars
      .map<AnimationPlan | null>((targetChar, index) => {
        const fromChar = fromPhrase[index] || ' '
        if (fromChar === targetChar) return null

        return {
          index,
          from: fromChar,
          target: targetChar,
          sequence: buildSequence(targetChar, safeFlips, activeCharset),
          start: index * safeStaggerMs,
          step: -1,
          done: false,
        }
      })
      .filter((plan): plan is AnimationPlan => plan !== null)

    if (!plans.length) {
      currentText = targetPhrase
      tiles.value = createTiles(targetPhrase)
      return 0
    }

    const totalDuration = plans.reduce(
      (max, plan) => Math.max(max, plan.start + plan.sequence.length * safeFlipMs),
      0,
    )
    const startedAt = performance.now()

    const updateTiles = (updates: TileUpdate[]) => {
      const nextTiles = [...tiles.value]
      updates.forEach((update) => {
        const tile = nextTiles[update.index]
        if (!tile) return

        nextTiles[update.index] = {
          current: update.current,
          next: update.next,
          flipping: !update.done,
          tick: tile.tick + 1,
        }
      })
      tiles.value = nextTiles
    }

    const tick = (now: number) => {
      if (cancelled) return

      const elapsed = now - startedAt
      const updates: TileUpdate[] = []
      let shouldContinue = false

      plans.forEach((plan) => {
        const localElapsed = elapsed - plan.start

        if (localElapsed < 0) {
          shouldContinue = true
          return
        }

        const step = Math.floor(localElapsed / safeFlipMs)

        if (step < plan.sequence.length) {
          shouldContinue = true

          if (step !== plan.step) {
            plan.step = step
            updates.push({
              index: plan.index,
              current: (step === 0 ? plan.from : plan.sequence[step - 1]) ?? ' ',
              next: plan.sequence[step] ?? ' ',
              done: false,
            })
          }
        } else if (!plan.done) {
          plan.done = true
          updates.push({
            index: plan.index,
            current: plan.target,
            next: plan.target,
            done: true,
          })
        }
      })

      if (updates.length > 0) updateTiles(updates)

      if (shouldContinue) {
        raf = requestAnimationFrame(tick)
      } else {
        currentText = targetPhrase
        raf = null
      }
    }

    raf = requestAnimationFrame(tick)
    return totalDuration
  }

  const scheduleNext = (delay: number) => {
    cycleTimer = window.setTimeout(() => {
      if (cancelled) return

      const nextIndex = phraseIndex + 1

      if (nextIndex >= normalizedPhrases.value.length && !props.loop) return

      phraseIndex = nextIndex % normalizedPhrases.value.length
      const animationDuration = animateTo(normalizedPhrases.value[phraseIndex] ?? '')
      scheduleNext(safeCycleDelay + animationDuration)
    }, delay)
  }

  // Flip the first phrase into view. Only keep cycling when there are more
  // phrases to show; a single phrase stays settled on the board afterwards.
  const revealDuration = animateTo(firstPhrase)

  if (normalizedPhrases.value.length > 1) {
    scheduleNext(safeCycleDelay + revealDuration)
  }

  teardownCycle = () => {
    cancelled = true
    clearAnimation()
  }
}

onMounted(() => {
  setupCycle()
})

onUnmounted(() => {
  teardownCycle?.()
  teardownCycle = null
})

watch(
  () => [
    normalizedPhrases.value,
    width.value,
    props.loop,
    props.cycleDelay,
    props.flipDuration,
    props.stagger,
    props.flipsPerChar,
    props.charset,
    prefersReducedMotion.value,
  ],
  () => {
    teardownCycle?.()
    teardownCycle = null
    setupCycle()
  },
  { deep: true },
)
</script>

<template>
  <div :class="[rootClass, className]" :style="componentStyle" role="text" :aria-label="settledText || undefined">
    <span
      v-for="(tile, index) in tiles"
      :key="`${index}-${tiles.length}`"
      :class="tileClass"
      aria-hidden="true"
    >
      <span :class="halfTopClass">
        <span :class="charTopClass">{{ tile.current === ' ' ? '\u00A0' : tile.current }}</span>
      </span>
      <span :class="halfBottomClass">
        <span :class="charBottomClass">{{ tile.flipping ? tile.next : tile.current }}</span>
      </span>

      <template v-if="tile.flipping">
        <span
          :key="`front-${index}-${tile.tick}`"
          :ref="(el: Element | ComponentPublicInstance | null) => playFrontFlip(el)"
          :class="flapFrontClass"
        >
          <span :class="charTopClass">{{ tile.current === ' ' ? '\u00A0' : tile.current }}</span>
        </span>
        <span
          :key="`back-${index}-${tile.tick}`"
          :ref="(el: Element | ComponentPublicInstance | null) => playBackFlip(el)"
          :class="flapBackClass"
        >
          <span :class="charBottomClass">{{ tile.next === ' ' ? '\u00A0' : tile.next }}</span>
        </span>
      </template>
    </span>
  </div>
</template>

<style scoped>
.split-flap {
  display: inline-flex;
  align-items: center;
  white-space: pre;
  user-select: none;
  line-height: 1;
  letter-spacing: 0.035em;
  font-variant-numeric: tabular-nums;
  font-weight: 760;
  font-family:
    'SFMono-Regular', 'Roboto Mono', 'Cascadia Code', 'Liberation Mono', Menlo, monospace;
  font-size: var(--split-flap-font-size, 52px);
  gap: var(--split-flap-gap, 6px);
}

.split-flap__tile {
  position: relative;
  width: 0.78em;
  height: 1.08em;
  overflow: hidden;
  border-radius: var(--split-flap-radius, 8px);
  background:
    radial-gradient(circle at 50% 0%, rgba(255, 255, 255, 0.16), transparent 44%),
    linear-gradient(
      180deg,
      color-mix(in srgb, var(--split-flap-tile-color, #111827) 82%, white),
      var(--split-flap-tile-color, #111827)
    );
  box-shadow:
    0 0.035em 0.08em rgba(255, 255, 255, 0.08) inset,
    0 -0.05em 0.1em rgba(0, 0, 0, 0.38) inset,
    0 0.16em 0.38em rgba(0, 0, 0, 0.28);
  perspective: 520px;
  transform-style: preserve-3d;
  isolation: isolate;
}

.split-flap__tile::before {
  content: '';
  position: absolute;
  z-index: 8;
  top: calc(50% - 0.5px);
  left: 0;
  width: 100%;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.18) 18%,
    rgba(0, 0, 0, 0.64) 50%,
    rgba(255, 255, 255, 0.14) 82%,
    transparent
  );
  box-shadow:
    0 -1px 0 rgba(255, 255, 255, 0.08),
    0 1px 0 rgba(0, 0, 0, 0.5);
  pointer-events: none;
}

.split-flap__tile::after {
  content: '';
  position: absolute;
  inset: 0;
  z-index: 9;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: inherit;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.2) inset;
  pointer-events: none;
}

.split-flap__half {
  position: absolute;
  left: 0;
  width: 100%;
  height: 50%;
  overflow: hidden;
  backface-visibility: hidden;
}

.split-flap__half--top {
  top: 0;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.07), transparent 34%),
    var(--split-flap-tile-color, #111827);
}

.split-flap__half--bottom {
  bottom: 0;
  background:
    linear-gradient(0deg, rgba(255, 255, 255, 0.06), transparent 38%),
    color-mix(in srgb, var(--split-flap-tile-color, #111827) 92%, black);
}

.split-flap__flap {
  position: absolute;
  left: 0;
  width: 100%;
  height: 50%;
  overflow: hidden;
  backface-visibility: hidden;
  z-index: 6;
  will-change: transform, filter;
  transform-style: preserve-3d;
}

.split-flap__flap--front {
  top: 0;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.07), transparent 34%),
    var(--split-flap-tile-color, #111827);
  transform-origin: center bottom;
}

.split-flap__flap--back {
  bottom: 0;
  background:
    linear-gradient(0deg, rgba(255, 255, 255, 0.06), transparent 38%),
    color-mix(in srgb, var(--split-flap-tile-color, #111827) 92%, black);
  transform-origin: center top;
  transform: rotateX(90deg);
}

.split-flap__char {
  position: absolute;
  left: 0;
  width: 100%;
  height: 200%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--split-flap-text-color, #f8fafc);
  text-shadow:
    0 0.025em 0 rgba(255, 255, 255, 0.16),
    0 0.09em 0.16em rgba(0, 0, 0, 0.42);
}

.split-flap__char--top {
  top: 0;
}

.split-flap__char--bottom {
  bottom: 0;
}
</style>
