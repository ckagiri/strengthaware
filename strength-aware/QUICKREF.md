# Quick Reference Card

## 🚀 Basic Usage

### Generate a Score
```java
TeamProfile home = new TeamProfile("Arsenal", "ARS", TeamStrength.ELITE, 94);
TeamProfile away = new TeamProfile("Leeds", "LEE", TeamStrength.RELEGATION, 40);

StrengthAwareScoreGenerator gen = new StrengthAwareScoreGenerator();
Score score = gen.generateScore(home, away);

System.out.println(score.format()); // "3-0"
```

### With Reproducible Seed
```java
StrengthAwareScoreGenerator gen = new StrengthAwareScoreGenerator(42L);
```

### Get Expected Goals
```java
double[] expected = gen.getExpectedGoals(home, away);
System.out.printf("Expected: %.2f - %.2f%n", expected[0], expected[1]);
```

## 📊 Team Ratings (12 Teams)

| Code | Team | Rating | Category |
|------|------|--------|----------|
| MCI | Manchester City | 95 | Elite |
| ARS | Arsenal | 94 | Elite |
| LIV | Liverpool | 90 | Elite |
| AVL | Aston Villa | 85 | Strong |
| CHE | Chelsea | 83 | Strong |
| NEW | Newcastle | 76 | Strong |
| MUN | Man United | 74 | Medium |
| TOT | Tottenham | 72 | Medium |
| BHA | Brighton | 70 | Medium |
| CRY | Crystal Palace | 68 | Weak |
| BRE | Brentford | 55 | Relegation |
| WHU | West Ham | 50 | Relegation |

## 🎯 Expected Outcomes

| Matchup | Win % | Most Likely Scores |
|---------|-------|-------------------|
| Elite vs Relegation (H) | 82% | 3-0, 4-0, 2-0 |
| Elite vs Elite (H) | 42% | 2-1, 1-1, 2-2 |
| Strong vs Medium (H) | 62% | 2-0, 2-1, 1-0 |
| Weak vs Relegation (H) | 58% | 1-0, 2-1, 1-1 |

## 📁 Files

| File | Purpose |
|------|---------|
| TeamStrength.java | 5 strength categories |
| TeamProfile.java | Team with ratings |
| Score.java | Match result |
| HomeAdvantageModifier.java | Home/away modifiers |
| StrengthAwareScoreGenerator.java | Main generator |
| PremierLeagueDemo.java | Demo with 12 teams |
| ScoreGeneratorTest.java | Validation tests |

## 🔧 Common Tasks

### Create Team
```java
TeamProfile team = new TeamProfile(
    "Team Name",
    "TLA",
    TeamStrength.STRONG,
    78  // rating
);
```

### Check Score Result
```java
Score score = gen.generateScore(home, away);

if (score.isHomeWin()) {
    System.out.println("Home victory!");
}
if (score.isDraw()) {
    System.out.println("Draw!");
}

int diff = score.getGoalDifference(); // +/- from home perspective
int total = score.getTotalGoals();
```

### Custom Home Advantage
```java
HomeAdvantageModifier custom = new HomeAdvantageModifier(
    1.20,  // home attack boost
    1.15,  // home defense boost
    0.90,  // away attack penalty
    0.85   // away defense penalty
);

StrengthAwareScoreGenerator gen = 
    new StrengthAwareScoreGenerator(custom, new Random());
```

### Neutral Venue
```java
HomeAdvantageModifier neutral = HomeAdvantageModifier.neutralVenue();
```

## 📊 Statistics

- **Average goals/game:** 2.7-3.0
- **Home win rate:** ~45%
- **Away win rate:** ~27%
- **Draw rate:** ~28%
- **Elite vs Relegation:** 80-85% win rate
- **Goals 0-2 per team:** ~80% of matches

## 🔬 Algorithm

```
Expected Goals = BASE (1.75) 
               × Attack Strength (0.5 to 1.5)
               × Home Modifier (0.90 to 1.15)
               × (1 - Defense Strength) (0.5 to 1.0)

Actual Goals = Poisson(Expected Goals)
             capped at 6
```

## ✅ Validation

Run tests:
```bash
./compile-and-test.sh
```

Or manually:
```bash
javac com/ligitabl/scoring/*.java
java com.ligitabl.scoring.ScoreGeneratorTest
java com.ligitabl.scoring.PremierLeagueDemo
```

## 🎓 Integration Example

```java
public class MatchSeeder {
    
    public void seedMatches() {
        StrengthAwareScoreGenerator gen = 
            new StrengthAwareScoreGenerator(42L);
        
        for (Match match : getScheduledMatches()) {
            TeamProfile home = getTeamProfile(match.getHomeId());
            TeamProfile away = getTeamProfile(match.getAwayId());
            
            Score score = gen.generateScore(home, away);
            
            match.setHomeGoals(score.getHomeGoals());
            match.setAwayGoals(score.getAwayGoals());
            match.setStatus("FINISHED");
            
            save(match);
        }
    }
}
```

## 📖 Full Documentation

See `README.md` for complete documentation, examples, and API reference.

---

**Package:** com.ligitabl.scoring  
**Version:** 1.0  
**Java:** 11+
