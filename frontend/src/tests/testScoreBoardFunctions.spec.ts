import { beforeEach, describe, expect, it } from 'vitest'
import { filterUsersByUserName } from '@/utils/ScoreBoard/filtering'
import type { UserStats } from '@/utils/ScoreBoard/types'

describe('scoreboard username search', () => {
  let users: UserStats[]

  beforeEach(() => {
    users = [
      {
        userId: 'id-alice',
        userName: 'Alice',
        score: 120,
        numberOfEasyChallengesSolved: 3,
        numberOfMediumChallengesSolved: 2,
        numberOfHardChallengesSolved: 1,
        totalChallengesSolved: 6,
      },
      {
        userId: 'id-bob',
        userName: 'bob',
        score: 80,
        numberOfEasyChallengesSolved: 1,
        numberOfMediumChallengesSolved: 0,
        numberOfHardChallengesSolved: 0,
        totalChallengesSolved: 1,
      },
      {
        userId: 'id-mallory',
        userName: 'Mallory',
        score: 40,
        numberOfEasyChallengesSolved: 0,
        numberOfMediumChallengesSolved: 0,
        numberOfHardChallengesSolved: 0,
        totalChallengesSolved: 0,
      },
    ]
  })

  it('returns every user when the search query is blank', () => {
    expect(filterUsersByUserName(users, '').length).toBe(3)
    expect(filterUsersByUserName(users, '   ').length).toBe(3)
    expect(filterUsersByUserName(users, null).length).toBe(3)
  })

  it('keeps only the users whose user name contains the query, ignoring case', () => {
    expect(filterUsersByUserName(users, 'ali').map((user) => user.userName)).toEqual(['Alice'])
    expect(filterUsersByUserName(users, 'BOB').map((user) => user.userName)).toEqual(['bob'])
    expect(filterUsersByUserName(users, 'al').map((user) => user.userName)).toEqual([
      'Alice',
      'Mallory',
    ])
  })

  it('returns no users when the query matches no user name', () => {
    const matches = filterUsersByUserName(users, 'nobody-named-this')

    expect(matches).toHaveLength(0)
  })
})