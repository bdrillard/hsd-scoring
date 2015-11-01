SELECT *, egg_drop_score + crack_safe_score + break_out_score AS score
FROM team_scores
ORDER BY team_name ASC
