UPDATE IGNORE team_scores
SET egg_drop_score = :value
WHERE team_name = :name
