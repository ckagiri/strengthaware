package com.ligitabl.scoring;

import java.util.Objects;

/**
 * Represents a soccer match score.
 * Immutable class containing home and away goal counts.
 */
public class Score {
    private final int homeGoals;
    private final int awayGoals;
    
    /**
     * Creates a score with the specified goal counts.
     * 
     * @param homeGoals Home team goals (must be non-negative)
     * @param awayGoals Away team goals (must be non-negative)
     * @throws IllegalArgumentException if either goal count is negative
     */
    public Score(int homeGoals, int awayGoals) {
        if (homeGoals < 0 || awayGoals < 0) {
            throw new IllegalArgumentException("Goal counts cannot be negative");
        }
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }
    
    public int getHomeGoals() {
        return homeGoals;
    }
    
    public int getAwayGoals() {
        return awayGoals;
    }
    
    /**
     * Gets the goal difference from the home team's perspective.
     * Positive means home win, negative means away win, zero means draw.
     */
    public int getGoalDifference() {
        return homeGoals - awayGoals;
    }
    
    /**
     * Checks if the home team won.
     */
    public boolean isHomeWin() {
        return homeGoals > awayGoals;
    }
    
    /**
     * Checks if the away team won.
     */
    public boolean isAwayWin() {
        return awayGoals > homeGoals;
    }
    
    /**
     * Checks if the match was a draw.
     */
    public boolean isDraw() {
        return homeGoals == awayGoals;
    }
    
    /**
     * Gets the total number of goals scored in the match.
     */
    public int getTotalGoals() {
        return homeGoals + awayGoals;
    }
    
    /**
     * Formats the score as "H-A" (e.g., "2-1", "0-0").
     */
    public String format() {
        return homeGoals + "-" + awayGoals;
    }
    
    /**
     * Formats the score with team names.
     */
    public String formatWithTeams(String homeTeam, String awayTeam) {
        return String.format("%s %d-%d %s", homeTeam, homeGoals, awayGoals, awayTeam);
    }
    
    /**
     * Gets a result indicator emoji.
     * 🏠 = home win, ✈️ = away win, 🤝 = draw
     */
    public String getResultIcon() {
        if (isHomeWin()) return "🏠";
        if (isAwayWin()) return "✈️";
        return "🤝";
    }
    
    @Override
    public String toString() {
        return format();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return homeGoals == score.homeGoals && awayGoals == score.awayGoals;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(homeGoals, awayGoals);
    }
}
