SELECT team_name, egg_drop_score + crack_safe_score + break_out_score AS score, school_name
FROM team_scores
WHERE disqualified = 0
ORDER BY score DESC
