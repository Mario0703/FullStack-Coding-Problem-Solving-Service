<script setup lang="ts">
import { animate, useReducedMotion, type AnimationPlaybackControls } from 'motion-v';
import {
  computed,
  defineComponent,
  h,
  onMounted,
  ref,
  useSlots,
  watch,
  type CSSProperties,
  type FunctionalComponent,
  type VNodeChild
} from 'vue';

export type ThoughtLineGlyph = 'sparkle' | 'dot' | 'none';

interface ThoughtLineProps {
  label?: string;
  doneLabel?: string;
  renderLabel?: (text: string, working: boolean) => VNodeChild;
  glyph?: ThoughtLineGlyph;
  steps?: string[];
  collapsible?: boolean;
  collapseOnSettle?: boolean;
  color?: string;
  glyphColor?: string;
  fontSize?: number;
  breathPeriod?: number;
  breathDepth?: number;
  shimmer?: boolean;
  shimmerDuration?: number;
  settleDuration?: number;
  settleBlur?: number;
  working?: boolean;
  settleAfter?: number;
  elapsed?: number;
  showTimer?: boolean;
  className?: string;
}

const EASE_OUT: [number, number, number, number] = [0.23, 1, 0.32, 1];
const EASE_IN_OUT: [number, number, number, number] = [0.77, 0, 0.175, 1];
const GLYPH_DONE = 0.55;

const fmt = (ds: number) =>
  ds < 600 ? `${(ds / 10).toFixed(1)}s` : `${Math.floor(ds / 600)}m ${((ds % 600) / 10).toFixed(1)}s`;
const spoken = (ds: number) =>
  ds < 600
    ? `${(ds / 10).toFixed(1)} seconds`
    : `${Math.floor(ds / 600)} minutes ${((ds % 600) / 10).toFixed(1)} seconds`;

const props = withDefaults(defineProps<ThoughtLineProps>(), {
  label: 'Thinking…',
  doneLabel: '',
  renderLabel: undefined,
  glyph: 'sparkle',
  steps: () => [],
  collapsible: true,
  collapseOnSettle: true,
  color: 'currentColor',
  glyphColor: '',
  fontSize: 16,
  breathPeriod: 1.6,
  breathDepth: 0.45,
  shimmer: true,
  shimmerDuration: 1.8,
  settleDuration: 350,
  settleBlur: 2,
  working: true,
  settleAfter: 0,
  elapsed: undefined,
  showTimer: true,
  className: ''
});

const emit = defineEmits<{ settle: [seconds: number] }>();

// The original uses a hand-made component so the head tag never resolves to a
// globally registered component; the same trick is kept here.
const HeadTag = defineComponent({
  props: { tag: { type: String, required: true } },
  setup(p, { attrs, slots }) {
    return () => h(p.tag, attrs, slots.default?.());
  }
});
const Rendered: FunctionalComponent<{
  fn: NonNullable<ThoughtLineProps['renderLabel']>;
  text: string;
  working: boolean;
}> = p => p.fn(p.text, p.working) as VNodeChild as never;

const slots = useSlots();
const reduce = useReducedMotion();
const autoSettled = ref(false);
const open = ref(true);
const isWorking = computed(() => props.working && !autoSettled.value);
const doneText = computed(() => props.doneLabel || (props.showTimer ? 'Thought for' : 'Done thinking'));
const hasTrace = computed(() => props.steps.length > 0);
const depth = computed(() => (reduce.value ? Math.min(props.breathDepth, 0.2) : props.breathDepth));
const period = computed(() => (reduce.value ? props.breathPeriod * 1.5 : props.breathPeriod));
const trough = computed(() => 1 - depth.value);
const sheen = computed(() => props.shimmer && !reduce.value);
const toggle = computed(() => hasTrace.value && props.collapsible);
const hasGlyph = computed(() => props.glyph !== 'none' || !!slots.glyph);

// post watchers need the refs, so they wait for this flag instead of running immediately
const mounted = ref(false);
onMounted(() => (mounted.value = true));
const glyphRef = ref<HTMLSpanElement | null>(null);
const breathRef = ref<HTMLSpanElement | null>(null);
const timerRef = ref<HTMLSpanElement | null>(null);
const stackRef = ref<HTMLSpanElement | null>(null);
const workRef = ref<HTMLSpanElement | null>(null);
const doneRef = ref<HTMLSpanElement | null>(null);
let ds = 0;
let prevWorking = isWorking.value;
const announce = ref(props.label);

watch(
  () => props.working,
  on => {
    if (on) autoSettled.value = false;
  }
);
watch(
  [isWorking, () => props.collapseOnSettle],
  () => {
    if (isWorking.value) open.value = true;
    else if (props.collapseOnSettle) open.value = false;
  },
  { immediate: true }
);

watch(
  [mounted, isWorking, period, depth, trough, () => props.settleDuration, () => props.glyph, sheen, hasGlyph],
  (_v, _o, onCleanup) => {
    const glyphEl = glyphRef.value;
    const breathEl = breathRef.value;
    if (!breathEl) return;
    const s = props.settleDuration / 1000;
    const tr = trough.value;
    const loop = (el: HTMLElement, delay: number) =>
      animate(el, { opacity: [tr, 1, tr] }, { duration: period.value, ease: EASE_IN_OUT, repeat: Infinity, delay });
    let cancelled = false;
    const running: AnimationPlaybackControls[] = [];
    if (isWorking.value) {
      if (depth.value > 0) {
        if (sheen.value) running.push(animate(breathEl, { opacity: 1 }, { duration: 0.2, ease: EASE_OUT }));
        if (glyphEl) {
          const lead = animate(glyphEl, { opacity: tr }, { duration: 0.2, ease: EASE_OUT });
          running.push(lead);
          lead.then(() => {
            if (cancelled) return;
            running.push(loop(glyphEl, 0));
            if (!sheen.value) running.push(loop(breathEl, 0.14));
          });
        } else if (!sheen.value) {
          running.push(loop(breathEl, 0.14));
        }
      } else {
        if (glyphEl) running.push(animate(glyphEl, { opacity: 1 }, { duration: 0.2, ease: EASE_OUT }));
        running.push(animate(breathEl, { opacity: 1 }, { duration: 0.2, ease: EASE_OUT }));
      }
    } else {
      if (glyphEl) running.push(animate(glyphEl, { opacity: GLYPH_DONE }, { duration: s, ease: EASE_OUT }));
      running.push(animate(breathEl, { opacity: 1 }, { duration: s, ease: EASE_OUT }));
    }
    onCleanup(() => {
      cancelled = true;
      running.forEach(a => a.stop());
    });
  },
  { flush: 'post' }
);

// the clock is written by hand so ten ticks a second never render the component
const paint = (next: number) => {
  ds = next;
  if (timerRef.value) timerRef.value.textContent = fmt(next);
};
watch(
  [mounted, isWorking, () => props.elapsed, () => props.settleAfter, () => props.showTimer],
  (_v, _o, onCleanup) => {
    if (props.elapsed != null) {
      paint(Math.round(props.elapsed * 10));
      return;
    }
    if (!isWorking.value) {
      paint(ds);
      return;
    }
    const startedAt = performance.now();
    paint(0);
    const id = setInterval(() => {
      const now = Math.floor((performance.now() - startedAt) / 100);
      paint(now);
      if (props.settleAfter > 0 && now >= Math.round(props.settleAfter * 10)) autoSettled.value = true;
    }, 100);
    onCleanup(() => clearInterval(id));
  },
  { flush: 'post' }
);

watch(
  [mounted, isWorking, () => props.label, doneText, () => props.fontSize, () => props.showTimer],
  (_v, _o, onCleanup) => {
    const t = timerRef.value;
    const stack = stackRef.value;
    if (!t || !stack) return;
    const place = (glide: boolean) => {
      const active = isWorking.value ? workRef.value : doneRef.value;
      if (!active) return;
      const shift = active.offsetWidth - stack.offsetWidth;
      if (!glide) t.style.transition = 'none';
      t.style.transform = `translateX(${shift}px)`;
      if (!glide) {
        void t.offsetWidth;
        t.style.transition = '';
      }
    };
    place(prevWorking !== isWorking.value);
    prevWorking = isWorking.value;
    const ro = new ResizeObserver(() => place(false));
    if (workRef.value) ro.observe(workRef.value);
    if (doneRef.value) ro.observe(doneRef.value);
    onCleanup(() => ro.disconnect());
  },
  { flush: 'post' }
);

watch(
  [mounted, isWorking],
  ([, working]) => {
    if (working) {
      announce.value = props.label;
      return;
    }
    announce.value = props.showTimer ? `${doneText.value} ${spoken(ds)}` : doneText.value;
    emit('settle', ds / 10);
  },
  { flush: 'post' }
);

const headAttrs = computed(() => {
  const cls = 'thought-line__head';
  if (!props.collapsible) return { class: cls };
  return {
    class: cls,
    type: 'button',
    'data-toggle': toggle.value ? '' : undefined,
    'aria-expanded': toggle.value ? open.value : undefined,
    tabindex: toggle.value ? 0 : -1,
    onClick: () => {
      if (toggle.value) open.value = !open.value;
    }
  };
});

const rootStyle = computed(
  () =>
    ({
      '--tl-font': `${props.fontSize}px`,
      '--tl-color': props.color,
      '--tl-glyph': props.glyphColor || props.color,
      '--tl-settle': `${props.settleDuration}ms`,
      '--tl-blur': `${props.settleBlur}px`,
      '--tl-shimmer': `${props.shimmerDuration}s`
    }) as CSSProperties
);
</script>

<template>
  <div
    class="thought-line"
    :class="className"
    :data-working="isWorking ? '' : undefined"
    :data-open="open && hasTrace ? '' : undefined"
    :style="rootStyle"
  >
    <HeadTag :tag="collapsible ? 'button' : 'div'" v-bind="headAttrs">
      <span
        v-if="hasGlyph"
        ref="glyphRef"
        class="thought-line__glyph"
        aria-hidden="true"
      >
        <slot name="glyph">
          <i v-if="glyph === 'sparkle'" class="mdi mdi-creation" aria-hidden="true"></i>
          <span v-else class="thought-line__dot" />
        </slot>
      </span>
      <span ref="stackRef" class="thought-line__stack" aria-hidden="true">
        <span ref="workRef" class="thought-line__work" :data-active="isWorking ? '' : undefined">
          <span ref="breathRef" class="thought-line__breath" :data-shimmer="sheen ? '' : undefined">
            <Rendered v-if="renderLabel" :fn="renderLabel" :text="label" :working="true" />
            <template v-else>{{ label }}</template>
          </span>
        </span>
        <span ref="doneRef" class="thought-line__done" :data-active="isWorking ? undefined : ''">
          <Rendered v-if="renderLabel" :fn="renderLabel" :text="doneText" :working="false" />
          <template v-else>{{ doneText }}</template>
        </span>
      </span>
      <span
        v-if="showTimer"
        ref="timerRef"
        class="thought-line__timer"
        :data-done="isWorking ? undefined : ''"
        aria-hidden="true"
      >
        0.0s
      </span>
      <span
        v-if="collapsible"
        class="thought-line__chevron"
        :data-on="hasTrace ? '' : undefined"
        aria-hidden="true"
      >
        <i class="mdi mdi-chevron-down" aria-hidden="true"></i>
      </span>
      <span class="thought-line__sr" role="status">{{ announce }}</span>
    </HeadTag>
    <div
      v-if="hasTrace"
      class="thought-line__trace"
      :data-open="open ? '' : undefined"
      :aria-hidden="!open"
    >
      <div class="thought-line__trace-clip">
        <div class="thought-line__steps">
          <div
            v-for="(text, i) in steps"
            :key="`${i}-${text}`"
            class="thought-line__step"
            :data-done="!isWorking || i < steps.length - 1 ? '' : undefined"
          >
            <span class="thought-line__step-badge" aria-hidden="true">
              <i v-if="!isWorking || i < steps.length - 1" class="mdi mdi-check" aria-hidden="true"></i>
              <i v-else class="thought-line__step-pulse" aria-hidden="true"></i>
            </span>
            <span class="thought-line__step-text">
              {{ text }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Tailwind utilities from the original Vue Bits snippet are expressed as the
   scoped classes below because this project does not ship Tailwind. The
   Hugeicons icons were swapped for the project's MDI icon font. */

.thought-line {
  display: inline-flex;
  flex-direction: column;
  align-items: flex-start;
  font-weight: 500;
  line-height: 1.2;
  color: var(--tl-color);
  font-family: inherit;
  font-size: var(--tl-font);
}

.thought-line__head {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 0.3em;
  margin: 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: inherit;
  font: inherit;
  text-align: left;
  white-space: nowrap;
  outline: none;
  cursor: default;
}

.thought-line__head[data-toggle] {
  cursor: pointer;
  -webkit-tap-highlight-color: transparent;
}

.thought-line__glyph {
  display: inline-flex;
  flex: none;
  width: 1.1em;
  height: 1.1em;
  margin-right: 0.2em;
  color: var(--tl-glyph);
}

.thought-line__glyph > i {
  font-size: 1.1em;
  line-height: 1;
}

.thought-line__dot {
  margin: auto;
  width: 0.5em;
  height: 0.5em;
  border-radius: 9999px;
  background: currentColor;
}

.thought-line__stack {
  display: inline-grid;
}

.thought-line__work,
.thought-line__done {
  grid-area: 1 / 1;
  width: max-content;
  opacity: 0;
  filter: blur(var(--tl-blur));
  transition:
    opacity var(--tl-settle) cubic-bezier(0.23, 1, 0.32, 1),
    filter var(--tl-settle) cubic-bezier(0.23, 1, 0.32, 1);
}

.thought-line__work[data-active],
.thought-line__done[data-active] {
  opacity: 1;
  filter: blur(0);
}

.thought-line__breath {
  display: inline-block;
}

/* Shimmer sweep across the working label; only while the line is working. */
.thought-line[data-working] .thought-line__breath[data-shimmer] {
  background-image: linear-gradient(
    100deg,
    color-mix(in srgb, var(--tl-color) 50%, transparent) 30%,
    var(--tl-color) 50%,
    color-mix(in srgb, var(--tl-color) 50%, transparent) 70%
  );
  background-size: 250% 100%;
  background-position: 125% 0;
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
  animation: thought-line-shimmer var(--tl-shimmer) linear infinite;
}

.thought-line__timer {
  font-variant-numeric: tabular-nums;
  transition:
    opacity var(--tl-settle) ease,
    transform var(--tl-settle) cubic-bezier(0.77, 0, 0.175, 1);
}

.thought-line__chevron {
  display: inline-flex;
  margin-left: 0.1em;
  opacity: 0;
  transition:
    opacity 200ms ease,
    transform 200ms cubic-bezier(0.23, 1, 0.32, 1);
}

.thought-line__chevron[data-on] {
  opacity: 0.55;
}

.thought-line[data-open] .thought-line__chevron {
  transform: rotate(180deg);
}

.thought-line__chevron > i {
  font-size: 1em;
  line-height: 1;
}

/* Visually hidden live region for screen readers. */
.thought-line__sr {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

/* Collapsible trace: grid-template-rows animates the fold. */
.thought-line__trace {
  display: grid;
  width: 0;
  min-width: 100%;
  grid-template-rows: 0fr;
  transition: grid-template-rows var(--tl-settle) cubic-bezier(0.23, 1, 0.32, 1);
}

.thought-line__trace[data-open] {
  grid-template-rows: 1fr;
}

.thought-line__trace-clip {
  min-height: 0;
  overflow-x: visible;
  overflow-y: clip;
}

.thought-line__steps {
  display: flex;
  flex-direction: column;
  gap: 0.45em;
  width: max-content;
  padding: 0.6em 0 0.2em 1.6em;
  font-weight: 400;
  font-size: 0.875em;
  white-space: nowrap;
}

.thought-line__step {
  display: flex;
  align-items: center;
  gap: 0.5em;
  transform: translateY(0);
  transition:
    opacity 200ms cubic-bezier(0.23, 1, 0.32, 1),
    transform 200ms cubic-bezier(0.23, 1, 0.32, 1);
}

/* Newly appended steps fade and slide in. */
@starting-style {
  .thought-line__step {
    opacity: 0;
    transform: translateY(-4px);
  }
}

.thought-line__step-badge {
  display: inline-grid;
  flex: none;
  place-items: center;
  width: 1em;
  height: 1em;
  opacity: 0.55;
}

.thought-line__step-badge > i {
  font-size: 1em;
  line-height: 1;
}

.thought-line__step-pulse {
  display: block;
  width: 0.4em;
  height: 0.4em;
  border-radius: 9999px;
  background: currentColor;
  animation: thought-line-pulse 1.6s cubic-bezier(0.77, 0, 0.175, 1) infinite;
}

.thought-line__step-text {
  transition: opacity var(--tl-settle) ease;
}

.thought-line__step[data-done] .thought-line__step-text {
  opacity: 0.55;
}

@keyframes thought-line-pulse {
  0%,
  100% {
    opacity: 0.55;
  }
  50% {
    opacity: 1;
  }
}

@keyframes thought-line-shimmer {
  to {
    background-position: -125% 0;
  }
}

@media (prefers-reduced-motion: reduce) {
  .thought-line__work,
  .thought-line__done {
    filter: none !important;
    transition: opacity var(--tl-settle) ease;
  }

  .thought-line__timer {
    transition: opacity var(--tl-settle) ease;
  }

  .thought-line__step {
    transform: none !important;
    transition: opacity 200ms ease;
  }

  .thought-line__step-pulse {
    animation-duration: 2.4s;
  }
}
</style>