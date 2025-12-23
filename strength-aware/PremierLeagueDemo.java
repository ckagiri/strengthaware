package com.ligitabl.scoring;

/**
 * Demonstration of the strength-aware score generator using real Premier League team data.
 * 
 * Uses 12 teams distributed across 5 strength categories (3-3-3-1-2):
 * - Elite (3 teams): Title contenders
 * - Strong (3 teams): European qualification level
 * - Medium (3 teams): Safe mid-table
 * - Weak (1 team): Relegation concerns
 * - Relegation (2 teams): Bottom tier
 * 
 * Based on 4-year performance data from the Premier League.
 */
public class PremierLeagueDemo {
    
    /**
     * Creates all 12 Premier League team profiles with realistic ratings.
     */
    public static TeamProfile[] createTeams() {
        return new TeamProfile[] {
            // Elite Tier (3 teams) - Ratings 85-95
            new TeamProfile("Manchester City", "MCI", TeamStrength.ELITE, 95),
            new TeamProfile("Arsenal", "ARS", TeamStrength.ELITE, 94),
            new TeamProfile("Liverpool", "LIV", TeamStrength.ELITE, 90),
            
            // Strong Tier (3 teams) - Ratings 70-84
            new TeamProfile("Aston Villa", "AVL", TeamStrength.STRONG, 85),
            new TeamProfile("Chelsea", "CHE", TeamStrength.STRONG, 83),
            new TeamProfile("Newcastle United", "NEW", TeamStrength.STRONG, 76),
            
            // Medium Tier (3 teams) - Ratings 55-69
            new TeamProfile("Manchester United", "MUN", TeamStrength.MEDIUM, 74),
            new TeamProfile("Tottenham Hotspur", "TOT", TeamStrength.MEDIUM, 72),
            new TeamProfile("Brighton", "BHA", TeamStrength.MEDIUM, 70),
            
            // Weak Tier (1 team) - Ratings 40-54
            new TeamProfile("Crystal Palace", "CRY", TeamStrength.WEAK, 68),
            
            // Relegation Tier (2 teams) - Ratings 0-39
            new TeamProfile("Brentford", "BRE", TeamStrength.RELEGATION, 55),
            new TeamProfile("West Ham United", "WHU", TeamStrength.RELEGATION, 50)
        };
    }
    
    /**
     * Demonstrates score generation for various matchup types.
     */
    public static void main(String[] args) {
        TeamProfile[] teams = createTeams();
        StrengthAwareScoreGenerator generator = new StrengthAwareScoreGenerator(2027L);
        
        System.out.println("=".repeat(80));
        System.out.println("PREMIER LEAGUE STRENGTH-AWARE SCORE GENERATOR DEMO");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Display all teams
        printTeams(teams);
        System.out.println();
        
        // Example 1: Elite vs Relegation (huge mismatch)
        demonstrateMatchup(
            teams[0],  // Man City (95)
            teams[11], // West Ham (50)
            generator,
            "Example 1: Elite vs Relegation - Dominant Home Win Expected"
        );
        
        // Example 2: Elite vs Elite (competitive match)
        demonstrateMatchup(
            teams[2],  // Liverpool (90)
            teams[1],  // Arsenal (94)
            generator,
            "Example 2: Elite vs Elite - Competitive Match"
        );
        
        // Example 3: Strong vs Medium (moderate advantage)
        demonstrateMatchup(
            teams[4],  // Chelsea (83)
            teams[7],  // Tottenham (72)
            generator,
            "Example 3: Strong vs Medium - Home Advantage Matters"
        );
        
        // Example 4: Weak vs Relegation (relegation battle)
        demonstrateMatchup(
            teams[9],  // Crystal Palace (68)
            teams[10], // Brentford (55)
            generator,
            "Example 4: Weak vs Relegation - Relegation Battle"
        );
        
        // Example 5: Away upset potential
        demonstrateMatchup(
            teams[11], // West Ham (50)
            teams[0],  // Man City (95)
            generator,
            "Example 5: Relegation vs Elite Away - Rare Upset Potential"
        );
        
        // Full round simulation
        System.out.println();
        simulateFullRound(teams, generator);
    }
    
    /**
     * Prints all team profiles.
     */
    private static void printTeams(TeamProfile[] teams) {
        System.out.println("TEAM PROFILES (3-3-3-1-2 Distribution)");
        System.out.println("-".repeat(80));
        
        TeamStrength currentCategory = null;
        for (TeamProfile team : teams) {
            if (currentCategory != team.getCategory()) {
                currentCategory = team.getCategory();
                System.out.println();
                System.out.printf("--- %s TIER ---%n", currentCategory);
            }
            System.out.printf("  %-25s [%s] Rating: %2d  Attack: %.2f  Defense: %.2f%n",
                team.getName(),
                team.getCode(),
                team.getRating(),
                team.getAttackingStrength(),
                team.getDefensiveStrength()
            );
        }
    }
    
    /**
     * Demonstrates a specific matchup with multiple score samples.
     */
    private static void demonstrateMatchup(
            TeamProfile home,
            TeamProfile away,
            StrengthAwareScoreGenerator generator,
            String description) {
        
        System.out.println("=".repeat(80));
        System.out.println(description);
        System.out.println("=".repeat(80));
        System.out.printf("Home: %s (%s) - Rating %d%n", home.getName(), home.getCode(), home.getRating());
        System.out.printf("Away: %s (%s) - Rating %d%n", away.getName(), away.getCode(), away.getRating());
        System.out.println();
        
        // Get expected goals
        double[] expected = generator.getExpectedGoals(home, away);
        System.out.printf("Expected Goals: Home %.2f - Away %.2f%n", expected[0], expected[1]);
        System.out.println();
        
        // Generate 10 sample scores
        System.out.println("Sample Scores:");
        int[] results = new int[3]; // [home wins, draws, away wins]
        
        for (int i = 0; i < 10; i++) {
            Score score = generator.generateScore(home, away);
            System.out.printf("  %s %-25s %s %-25s %s%n",
                score.getResultIcon(),
                home.getName(),
                score.format(),
                away.getName(),
                getResultDescription(score)
            );
            
            if (score.isHomeWin()) results[0]++;
            else if (score.isDraw()) results[1]++;
            else results[2]++;
        }
        
        System.out.println();
        System.out.printf("Results: %d home wins, %d draws, %d away wins%n",
            results[0], results[1], results[2]);
        System.out.println();
    }
    
    /**
     * Simulates a full round of matches (6 matches from 12 teams).
     */
    private static void simulateFullRound(TeamProfile[] teams, StrengthAwareScoreGenerator generator) {
        System.out.println("=".repeat(80));
        System.out.println("SAMPLE FULL ROUND (Round 1)");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Create 6 matches (simple pairing)
        int[][] fixtures = {
            {0, 11},   // Man City vs West Ham
            {10, 1},   // Brentford vs Arsenal
            {2, 9},    // Liverpool vs Crystal Palace
            {8, 3},    // Brighton vs Aston Villa
            {4, 7},    // Chelsea vs Tottenham
            {6, 5}     // Man United vs Newcastle
        };
        
        for (int[] fixture : fixtures) {
            TeamProfile home = teams[fixture[0]];
            TeamProfile away = teams[fixture[1]];
            Score score = generator.generateScore(home, away);
            
            System.out.printf("  %-25s %s  %-25s %s%n",
                home.getName(),
                score.format(),
                away.getName(),
                score.getResultIcon()
            );
        }
        
        System.out.println();
        System.out.println("Legend: 🏠 = Home Win | ✈️ = Away Win | 🤝 = Draw");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Gets a description of the match result.
     */
    private static String getResultDescription(Score score) {
        int diff = Math.abs(score.getGoalDifference());
        String result = score.isHomeWin() ? "Home win" : 
                       score.isAwayWin() ? "Away win" : "Draw";
        
        if (diff == 0) return result;
        if (diff == 1) return result + " (narrow)";
        if (diff == 2) return result + " (comfortable)";
        return result + " (dominant)";
    }
}
