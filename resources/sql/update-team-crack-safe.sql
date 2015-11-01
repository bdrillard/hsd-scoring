UPDATE IGNORE team_scores
SET crack_safe_score = :value
WHERE team_name = :name
