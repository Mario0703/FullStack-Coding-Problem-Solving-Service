<script setup lang="ts">
import FadeContent from '@/components/ui/FadeContent.vue'
import Footer from '@/components/navigation/Footer.vue'
import LogoLoop, { type LogoItem } from '@/components/ui/LogoLoop.vue'
import SplitFlapText from '@/components/home/SplitFlapText.vue'

const supportedLanguageLogos: LogoItem[] = [
  {
    node: '<i class="mdi mdi-language-python"></i>',
    title: 'Python',
    href: 'https://www.python.org/',
  },
  {
    node: '<i class="mdi mdi-language-java"></i>',
    title: 'Java',
    href: 'https://www.java.com/',
  },
  {
    node: '<i class="mdi mdi-language-csharp"></i>',
    title: 'C#',
    href: 'https://dotnet.microsoft.com/languages/csharp',
  },
  {
    node: '<i class="mdi mdi-language-cpp"></i>',
    title: 'C++',
    href: 'https://isocpp.org/',
  },
]
const techLogos: LogoItem[] = [
  {
    src: '/logos/google.svg',
    alt: 'Google',
    href: 'https://www.google.com/',
  },
  {
    src: '/logos/apple.svg',
    alt: 'Apple',
    href: 'https://www.apple.com/',
  },
  {
    src: '/logos/netflix.svg',
    alt: 'Netflix',
    href: 'https://www.netflix.com/',
  },
  {
    src: '/logos/meta.svg',
    alt: 'Meta',
    href: 'https://about.meta.com/',
  },
  {
    src: '/logos/nvidia.svg',
    alt: 'NVIDIA',
    href: 'https://www.nvidia.com/',
  },
  {
    src: '/logos/uber.svg',
    alt: 'Uber',
    href: 'https://www.uber.com/',
  },
  {
    src: '/logos/airbnb.svg',
    alt: 'Airbnb',
    href: 'https://www.airbnb.com/',
  },
  {
    src: '/logos/tesla.svg',
    alt: 'Tesla',
    href: 'https://www.tesla.com/',
  },
  {
    src: '/logos/spotify.svg',
    alt: 'Spotify',
    href: 'https://www.spotify.com/',
  },
]
</script>

<template>
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container>
      <v-card variant="text">
        <v-card-title class="px-0">
          <SplitFlapText
            text="MiniLeetCode"
            tile-color="#212121"
            text-color="#ffffff"
            font-size="clamp(2.25rem, 7vw, 5rem)"
          />
        </v-card-title>
        <v-card-text class="px-0 text-body-medium">
          MiniLeetCode is designed for anyone wanting to learn programming fundamentals through
          bite-sized problems. Build skills and practice solving common algorithmic tasks efficiently
        </v-card-text>
        <v-card-actions class="mt-5 px-0">
          <RouterLink to="/Signup">
            <v-btn variant="elevated" color="grey-darken-4" size="large" prepend-icon="mdi-account-plus"> Create an account</v-btn>
          </RouterLink>
        </v-card-actions>
      </v-card>
    </v-container>

    <v-container>
        <v-card variant="text">
          <v-card-title class="px-0 font-weight-bold">What does this service offer</v-card-title>
          <v-card-text class="px-0 py-0">
            This service offers a variety of coding challenges you can solve.
          </v-card-text>
          <v-card-text class="px-0 py-0">
            You choose your favorite programming language and start solving the challenges immediately.
          </v-card-text>
          <v-card-text class="px-0 py-0">
            You can select curated sets of diverse challenges suited perfectly for your skill level and
            begin practicing right away.
          </v-card-text>
          <v-card-actions class="mt-5 px-0">
            <RouterLink to="/challenges">
              <v-btn variant="elevated" color="grey-darken-4" size="large" prepend-icon="mdi-eye">Start by viewing available challenges</v-btn>
            </RouterLink>
          </v-card-actions>
        </v-card>

        <v-card variant="text">
          <v-card-title class="px-0 font-weight-bold">What this service supports</v-card-title>
          <v-card-text class="text-body-1 px-0 py-0">
            This platform provides a wide selection of coding challenges to enhance your programming
            skills.
          </v-card-text>
          <v-card-text class="text-body-1 px-0 mb-7 py-0">
            You can choose from supported languages (Python, C#, Java, and C++) and use the challenge
            editor to write and test your solution. You can also view the results of your solution and compare it with other users' solutions.
            Solve challanges on the form
          </v-card-text>
          <v-list bg-color="transparent" rounded lines="two">
            <v-list-item title="Array" />
            <v-divider />
            <v-list-item title="Linked List" />
            <v-divider />
            <v-list-item title="Binary Tree" />
          </v-list>
          <v-card-text class="text-body-1 px-0 pt-6 pb-0">
            Solve challenges in the languages you already use:
          </v-card-text>
          <div
            style="
              height: 200px;
              position: relative;
              overflow: hidden;
            "
                  >
          <LogoLoop
            :logos="supportedLanguageLogos"
            :speed="120"
            direction="left"
            :logo-height="40"
            :gap="48"
            :hover-speed="30"
            :scale-on-hover="true"
            :fade-out="true"
            fade-out-color="#ffffff"
            aria-label="Supported programming languages"
            class="mt-4 mb-2"
          />
          </div>
        </v-card>

        <v-card variant="text">
          <v-card-title class="px-0 font-weight-bold">How MiniLeetCode works</v-card-title>
          <v-card-text class="px-0 py-0">
            Getting started takes less than a minute. Create a free account, pick a challenge,
            and solve it directly in your browser.
          </v-card-text>
          <v-card-text class="px-0 py-0">
            Write your solution in the built-in editor, choose from Python, Java, C#, or C++, and
            submit it to run the test cases instantly. You will see exactly which cases pass and
            which still need work, so you always know what to improve next.
          </v-card-text>
          <v-list bg-color="transparent" rounded lines="two">
            <v-list-item title="1. Create your free account" />
            <v-divider />
            <v-list-item title="2. Choose a challenge by topic or difficulty" />
            <v-divider />
            <v-list-item title="3. Solve it, submit it, and track your progress" />
          </v-list>
          <v-card-actions class="mt-5 px-0">
            <RouterLink to="/Signup">
              <v-btn color="grey-darken-4" size="large" variant="elevated">Get started for free</v-btn>
            </RouterLink>
          </v-card-actions>
        </v-card>

      <v-card variant="text">
        <v-card-title class="px-0 font-weight-bold">Questions asked by</v-card-title>
        <LogoLoop
          :logos="techLogos"
          :speed="120"
          direction="left"
          :logo-height="40"
          :gap="48"
          :hover-speed="30"
          :scale-on-hover="true"
          :fade-out="true"
          fade-out-color="#ffffff"
          aria-label="Tech companies our questions are based on"
          class="mt-4 mb-2"
        />

      </v-card>

        <v-card variant="text">
          <v-card-title class="px-0 font-weight-bold">Why practice with MiniLeetCode</v-card-title>
          <v-card-text class="px-0 py-0">
            Consistent, bite-sized practice is the fastest way to build real problem-solving
            skills. Every challenge is small enough to finish in one sitting, so you can keep a
            steady habit without feeling overwhelmed.
          </v-card-text>
          <v-card-text class="px-0 py-0">
            Work through arrays, linked lists, binary trees and more, then review your submissions
            to spot patterns and reinforce what you have learned. Whether you are preparing for
            interviews or simply sharpening your fundamentals, there is always a next challenge to
            grow with.
          </v-card-text>
          <v-card-actions class="mt-5 px-0">
            <RouterLink to="/challenges">
              <v-btn color="grey-darken-4" size="large" variant="elevated">Browse all challenges</v-btn>
            </RouterLink>
          </v-card-actions>
        </v-card>
    </v-container>

    <Footer />
  </FadeContent>
</template>
