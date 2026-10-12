import { describe, expect, it } from 'vitest'
import { difficultyChip } from '@/utils/Challenges/Types/options'

describe('difficulty chip styling', () => {
  it('shows a green leaf for easy challenges', () => {
    expect(difficultyChip('EASY')).toEqual({ color: 'success', prependIcon: 'mdi-leaf' })
  })

  it('shows an orange fire for medium challenges', () => {
    expect(difficultyChip('MEDIUM')).toEqual({ color: 'warning', prependIcon: 'mdi-fire' })
  })

  it('shows a red skull for hard challenges', () => {
    expect(difficultyChip('HARD')).toEqual({ color: 'error', prependIcon: 'mdi-skull' })
  })
})