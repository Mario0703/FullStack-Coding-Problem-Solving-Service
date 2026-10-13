<template>
  <div
    ref="containerRef"
    :class="rootClasses"
    :style="containerStyle"
    role="region"
    :aria-label="ariaLabel"
  >
    <template v-if="fadeOut">
      <template v-if="isVertical">
        <div aria-hidden="true" class="logo-loop__fade logo-loop__fade--top" />
        <div aria-hidden="true" class="logo-loop__fade logo-loop__fade--bottom" />
      </template>

      <template v-else>
        <div aria-hidden="true" class="logo-loop__fade logo-loop__fade--left" />
        <div aria-hidden="true" class="logo-loop__fade logo-loop__fade--right" />
      </template>
    </template>

    <div
      ref="trackRef"
      :class="trackClasses"
      @mouseenter="handleMouseEnter"
      @mouseleave="handleMouseLeave"
    >
      <ul
        v-for="copyIndex in copyCount"
        :key="`copy-${copyIndex - 1}`"
        :class="sequenceClasses"
        role="list"
        :aria-hidden="copyIndex > 1 ? true : undefined"
        :ref="
          el => {
            if (copyIndex === 1) seqRef = el as HTMLUListElement | null;
          }
        "
      >
        <li
          v-for="(item, itemIndex) in logos"
          :key="`${copyIndex - 1}-${itemIndex}`"
          :class="itemClasses"
          role="listitem"
        >
          <slot
            v-if="$slots.renderItem"
            name="renderItem"
            :item="item"
            :index="`${copyIndex - 1}-${itemIndex}`"
          />
          <template v-else>
            <a
              v-if="item.href"
              class="logo-loop__link"
              :href="item.href"
              :aria-label="getItemAriaLabel(item) || 'logo link'"
              target="_blank"
              rel="noreferrer noopener"
            >
              <LogoContent :item="item" />
            </a>
            <LogoContent v-else :item="item" />
          </template>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref, watch, type CSSProperties } from 'vue';

export type LogoItemNode = {
  node: string;
  href?: string;
  title?: string;
  ariaLabel?: string;
};

export type LogoItemImage = {
  src: string;
  alt?: string;
  href?: string;
  title?: string;
  srcSet?: string;
  sizes?: string;
  width?: number;
  height?: number;
};

export type LogoItem = LogoItemNode | LogoItemImage;

export interface LogoLoopProps {
  logos: LogoItem[];
  speed?: number;
  direction?: 'left' | 'right' | 'up' | 'down';
  width?: number | string;
  logoHeight?: number;
  gap?: number;
  pauseOnHover?: boolean;
  hoverSpeed?: number;
  fadeOut?: boolean;
  fadeOutColor?: string;
  scaleOnHover?: boolean;
  ariaLabel?: string;
  className?: string;
  style?: CSSProperties;
}

const ANIMATION_CONFIG = {
  SMOOTH_TAU: 0.25,
  MIN_COPIES: 2,
  COPY_HEADROOM: 2
} as const;

const props = withDefaults(defineProps<LogoLoopProps>(), {
  speed: 120,
  direction: 'left',
  width: '100%',
  logoHeight: 28,
  gap: 32,
  pauseOnHover: undefined,
  hoverSpeed: undefined,
  fadeOut: false,
  scaleOnHover: false,
  ariaLabel: 'Partner logos'
});

const containerRef = ref<HTMLDivElement | null>(null);
const trackRef = ref<HTMLDivElement | null>(null);
const seqRef = ref<HTMLUListElement | null>(null);

const seqWidth = ref<number>(0);
const seqHeight = ref<number>(0);
const copyCount = ref<number>(ANIMATION_CONFIG.MIN_COPIES);
const isHovered = ref<boolean>(false);

let rafRef: number | null = null;
let lastTimestampRef: number | null = null;
const offsetRef = ref(0);
const velocityRef = ref(0);

const isVertical = computed(() => props.direction === 'up' || props.direction === 'down');

const effectiveHoverSpeed = computed<number | undefined>(() => {
  if (props.hoverSpeed !== undefined) return props.hoverSpeed;
  if (props.pauseOnHover === true) return 0;
  if (props.pauseOnHover === false) return undefined;
  return 0; // default: pause on hover
});

const targetVelocity = computed(() => {
  const magnitude = Math.abs(props.speed);
  let directionMultiplier: number;
  if (isVertical.value) {
    directionMultiplier = props.direction === 'up' ? 1 : -1;
  } else {
    directionMultiplier = props.direction === 'left' ? 1 : -1;
  }
  const speedMultiplier = props.speed < 0 ? -1 : 1;
  return magnitude * directionMultiplier * speedMultiplier;
});

const cssVariables = computed(() => ({
  '--logoloop-gap': `${props.gap}px`,
  '--logoloop-logoHeight': `${props.logoHeight}px`,
  ...(props.fadeOutColor ? { '--logoloop-fadeColor': props.fadeOutColor } : {})
}));

// Tailwind utilities from the original snippet are expressed as scoped classes
// (see <style scoped> below) because this project does not ship Tailwind.
const rootClasses = computed(() =>
  [
    'logo-loop',
    isVertical.value ? 'logo-loop--vertical' : 'logo-loop--horizontal',
    props.scaleOnHover && 'logo-loop--scalable',
    props.className
  ].filter((cls): cls is string => Boolean(cls))
);

const trackClasses = computed(() =>
  [
    'logo-loop__track',
    isVertical.value ? 'logo-loop__track--vertical' : 'logo-loop__track--horizontal'
  ].filter((cls): cls is string => Boolean(cls))
);

const sequenceClasses = computed(() =>
  [
    'logo-loop__sequence',
    isVertical.value && 'logo-loop__sequence--vertical'
  ].filter((cls): cls is string => Boolean(cls))
);

const itemClasses = computed(() =>
  [
    'logo-loop__item',
    isVertical.value ? 'logo-loop__item--vertical' : 'logo-loop__item--horizontal',
    props.scaleOnHover && 'logo-loop__item--scalable'
  ].filter((cls): cls is string => Boolean(cls))
);

const containerStyle = computed(() => {
  const w = typeof props.width === 'number' ? `${props.width}px` : props.width;
  return {
    ...(isVertical.value ? (w !== '100%' && w ? { width: w } : {}) : { width: w ?? '100%' }),
    ...cssVariables.value,
    ...props.style
  };
});

const isNodeItem = (item: LogoItem): item is LogoItemNode => 'node' in item;

const getItemAriaLabel = (item: LogoItem): string | undefined => {
  if (isNodeItem(item)) return item.ariaLabel ?? item.title;
  return item.alt ?? item.title;
};

const handleMouseEnter = () => {
  if (effectiveHoverSpeed.value !== undefined) isHovered.value = true;
};

const handleMouseLeave = () => {
  if (effectiveHoverSpeed.value !== undefined) isHovered.value = false;
};

const updateDimensions = async () => {
  await nextTick();

  const containerWidth = containerRef.value?.clientWidth ?? 0;
  const sequenceRect = seqRef.value?.getBoundingClientRect?.();
  const sequenceWidth = sequenceRect?.width ?? 0;
  const sequenceHeight = sequenceRect?.height ?? 0;

  if (isVertical.value) {
    const parentHeight = containerRef.value?.parentElement?.clientHeight ?? 0;
    if (containerRef.value && parentHeight > 0) {
      const targetHeight = Math.ceil(parentHeight);
      if (containerRef.value.style.height !== `${targetHeight}px`) {
        containerRef.value.style.height = `${targetHeight}px`;
      }
    }
    if (sequenceHeight > 0) {
      seqHeight.value = Math.ceil(sequenceHeight);
      const viewport = containerRef.value?.clientHeight ?? parentHeight ?? sequenceHeight;
      const copiesNeeded = Math.ceil(viewport / sequenceHeight) + ANIMATION_CONFIG.COPY_HEADROOM;
      copyCount.value = Math.max(ANIMATION_CONFIG.MIN_COPIES, copiesNeeded);
    }
  } else if (sequenceWidth > 0) {
    seqWidth.value = Math.ceil(sequenceWidth);
    const copiesNeeded = Math.ceil(containerWidth / sequenceWidth) + ANIMATION_CONFIG.COPY_HEADROOM;
    copyCount.value = Math.max(ANIMATION_CONFIG.MIN_COPIES, copiesNeeded);
  }
};

let resizeObserver: ResizeObserver | null = null;

const setupResizeObserver = () => {
  if (!window.ResizeObserver) {
    window.addEventListener('resize', updateDimensions);
    updateDimensions();
    return () => window.removeEventListener('resize', updateDimensions);
  }

  resizeObserver = new ResizeObserver(updateDimensions);
  if (containerRef.value) resizeObserver.observe(containerRef.value);
  if (seqRef.value) resizeObserver.observe(seqRef.value);
  updateDimensions();

  return () => {
    resizeObserver?.disconnect();
    resizeObserver = null;
  };
};

const setupImageLoader = () => {
  const images = seqRef.value?.querySelectorAll('img') ?? [];

  if (images.length === 0) {
    updateDimensions();
    return () => {};
  }

  let remaining = images.length;
  const handleLoad = () => {
    remaining -= 1;
    if (remaining === 0) updateDimensions();
  };

  images.forEach(img => {
    const htmlImg = img as HTMLImageElement;
    if (htmlImg.complete) {
      handleLoad();
    } else {
      htmlImg.addEventListener('load', handleLoad, { once: true });
      htmlImg.addEventListener('error', handleLoad, { once: true });
    }
  });

  return () => {
    images.forEach(img => {
      img.removeEventListener('load', handleLoad);
      img.removeEventListener('error', handleLoad);
    });
  };
};

const startAnimationLoop = () => {
  const track = trackRef.value;
  if (!track) return () => {};

  const prefersReduced =
    typeof window !== 'undefined' && window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;

  const seqSize = isVertical.value ? seqHeight.value : seqWidth.value;

  if (seqSize > 0) {
    offsetRef.value = ((offsetRef.value % seqSize) + seqSize) % seqSize;
    track.style.transform = isVertical.value
      ? `translate3d(0, ${-offsetRef.value}px, 0)`
      : `translate3d(${-offsetRef.value}px, 0, 0)`;
  }

  if (prefersReduced) {
    track.style.transform = 'translate3d(0, 0, 0)';
    return () => {
      lastTimestampRef = null;
    };
  }

  const animate = (timestamp: number) => {
    if (lastTimestampRef === null) lastTimestampRef = timestamp;

    const deltaTime = Math.max(0, timestamp - lastTimestampRef) / 1000;
    lastTimestampRef = timestamp;

    const target =
      isHovered.value && effectiveHoverSpeed.value !== undefined ? effectiveHoverSpeed.value : targetVelocity.value;

    const easingFactor = 1 - Math.exp(-deltaTime / ANIMATION_CONFIG.SMOOTH_TAU);
    velocityRef.value += (target - velocityRef.value) * easingFactor;

    const currentSeqSize = isVertical.value ? seqHeight.value : seqWidth.value;

    if (currentSeqSize > 0) {
      let nextOffset = offsetRef.value + velocityRef.value * deltaTime;
      nextOffset = ((nextOffset % currentSeqSize) + currentSeqSize) % currentSeqSize;
      offsetRef.value = nextOffset;

      track.style.transform = isVertical.value
        ? `translate3d(0, ${-offsetRef.value}px, 0)`
        : `translate3d(${-offsetRef.value}px, 0, 0)`;
    }

    rafRef = requestAnimationFrame(animate);
  };

  rafRef = requestAnimationFrame(animate);

  return () => {
    if (rafRef !== null) {
      cancelAnimationFrame(rafRef);
      rafRef = null;
    }
    lastTimestampRef = null;
  };
};

let cleanupResize: (() => void) | undefined;
let cleanupImages: (() => void) | undefined;
let cleanupAnimation: (() => void) | undefined;

const cleanup = () => {
  cleanupResize?.();
  cleanupImages?.();
  cleanupAnimation?.();
};

onMounted(async () => {
  await nextTick();
  setTimeout(() => {
    cleanupResize = setupResizeObserver();
    cleanupImages = setupImageLoader();
    cleanupAnimation = startAnimationLoop();
  }, 10);
});

onUnmounted(() => {
  cleanup();
});

watch(
  [() => props.logos, () => props.gap, () => props.logoHeight, () => props.direction],
  async () => {
    await nextTick();
    cleanupImages?.();
    cleanupImages = setupImageLoader();
    cleanupAnimation?.();
    cleanupAnimation = startAnimationLoop();
  },
  { deep: true }
);

watch([() => props.speed, () => props.direction, () => props.hoverSpeed, () => props.pauseOnHover], () => {
  cleanupAnimation?.();
  cleanupAnimation = startAnimationLoop();
});
</script>

<script lang="ts">
import { defineComponent, h } from 'vue';

const LogoContent = defineComponent({
  name: 'LogoContent',
  props: {
    item: {
      type: Object as () => LogoItem,
      required: true
    }
  },
  setup(props) {
    const isNodeItem = (item: LogoItem): item is LogoItemNode => 'node' in item;

    return () => {
      if (isNodeItem(props.item)) {
        return h('span', {
          class: 'logo-loop__logo',
          innerHTML: props.item.node,
          'aria-hidden':
            !!(props.item as LogoItemNode).href && !(props.item as LogoItemNode).ariaLabel ? true : undefined
        });
      }

      return h('img', {
        class: 'logo-loop__logo',
        src: props.item.src,
        srcset: (props.item as LogoItemImage).srcSet,
        sizes: (props.item as LogoItemImage).sizes,
        width: (props.item as LogoItemImage).width,
        height: (props.item as LogoItemImage).height,
        alt: (props.item as LogoItemImage).alt ?? '',
        title: props.item.title,
        loading: 'lazy',
        decoding: 'async',
        draggable: false
      });
    };
  }
});

export { LogoContent };
</script>

<style scoped>
/* Tailwind utilities from the original Vue Bits snippet are expressed as the
   scoped classes below because this project does not ship Tailwind. */

.logo-loop {
  position: relative;
  --logoloop-gap: 32px;
  --logoloop-logoHeight: 28px;
  --logoloop-fadeColorAuto: #ffffff;
}

@media (prefers-color-scheme: dark) {
  .logo-loop {
    --logoloop-fadeColorAuto: #0b0b0b;
  }
}

.logo-loop--horizontal {
  overflow-x: hidden;
}

.logo-loop--vertical {
  overflow: hidden;
  height: 100%;
  display: inline-block;
}

/* Headroom so scaled logos are not clipped by the hidden overflow. */
.logo-loop--scalable {
  padding-top: calc(var(--logoloop-logoHeight) * 0.1);
  padding-bottom: calc(var(--logoloop-logoHeight) * 0.1);
}

.logo-loop__fade {
  position: absolute;
  pointer-events: none;
  z-index: 10;
}

.logo-loop__fade--top {
  top: 0;
  left: 0;
  right: 0;
  height: clamp(24px, 8%, 120px);
  background: linear-gradient(
    to bottom,
    var(--logoloop-fadeColor, var(--logoloop-fadeColorAuto)) 0%,
    rgba(0, 0, 0, 0) 100%
  );
}

.logo-loop__fade--bottom {
  bottom: 0;
  left: 0;
  right: 0;
  height: clamp(24px, 8%, 120px);
  background: linear-gradient(
    to top,
    var(--logoloop-fadeColor, var(--logoloop-fadeColorAuto)) 0%,
    rgba(0, 0, 0, 0) 100%
  );
}

.logo-loop__fade--left {
  left: 0;
  top: 0;
  bottom: 0;
  width: clamp(24px, 8%, 120px);
  background: linear-gradient(
    to right,
    var(--logoloop-fadeColor, var(--logoloop-fadeColorAuto)) 0%,
    rgba(0, 0, 0, 0) 100%
  );
}

.logo-loop__fade--right {
  right: 0;
  top: 0;
  bottom: 0;
  width: clamp(24px, 8%, 120px);
  background: linear-gradient(
    to left,
    var(--logoloop-fadeColor, var(--logoloop-fadeColorAuto)) 0%,
    rgba(0, 0, 0, 0) 100%
  );
}

.logo-loop__track {
  display: flex;
  position: relative;
  z-index: 0;
  will-change: transform;
  user-select: none;
}

.logo-loop__track--horizontal {
  flex-direction: row;
  width: max-content;
}

.logo-loop__track--vertical {
  flex-direction: column;
  height: max-content;
  width: 100%;
}

@media (prefers-reduced-motion: reduce) {
  .logo-loop__track {
    transform: none !important;
  }
}

.logo-loop__sequence {
  display: flex;
  align-items: center;
  /* Tailwind preflight resets list styles; this project does not ship
     Tailwind, so reset the browser defaults here to avoid bullet points. */
  list-style: none;
  margin: 0;
  padding: 0;
}

.logo-loop__sequence--vertical {
  flex-direction: column;
}

.logo-loop__item {
  flex: none;
  font-size: var(--logoloop-logoHeight);
  line-height: 1;
}

.logo-loop__item--horizontal {
  margin-right: var(--logoloop-gap);
}

.logo-loop__item--vertical {
  margin-bottom: var(--logoloop-gap);
}

.logo-loop__item--scalable {
  overflow: visible;
}

.logo-loop__link {
  display: inline-flex;
  align-items: center;
  text-decoration: none;
  border-radius: 0.25rem;
  transition: opacity 200ms linear;
}

.logo-loop__link:hover {
  opacity: 0.8;
}

.logo-loop__link:focus-visible {
  outline: 2px solid currentColor;
  outline-offset: 2px;
}

/* Logos are rendered by the internal LogoContent helper, hence :deep(). */
.logo-loop__item :deep(.logo-loop__logo) {
  display: inline-flex;
  align-items: center;
  font-size: var(--logoloop-logoHeight);
  line-height: 1;
}

.logo-loop__item :deep(.logo-loop__logo > i) {
  font-size: var(--logoloop-logoHeight);
  line-height: 1;
}

.logo-loop__item :deep(img.logo-loop__logo) {
  display: block;
  height: var(--logoloop-logoHeight);
  width: auto;
  object-fit: contain;
  pointer-events: none;
  -webkit-user-drag: none;
  image-rendering: -webkit-optimize-contrast;
}

.logo-loop__item--scalable :deep(.logo-loop__logo) {
  transition: transform 300ms cubic-bezier(0.4, 0, 0.2, 1);
}

.logo-loop__item--scalable:hover :deep(.logo-loop__logo) {
  transform: scale(1.2);
}

@media (prefers-reduced-motion: reduce) {
  .logo-loop__item :deep(.logo-loop__logo) {
    transition: none;
  }
}
</style>