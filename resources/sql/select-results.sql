SELECT team_name, launch_score + ramp_score AS score, weight
FROM team_scores
WHERE disqualified = 0
ORDER BY score DESC, weight ASC
