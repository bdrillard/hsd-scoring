UPDATE IGNORE team_scores
SET ramp_score = :value
WHERE team_name = :name
