UPDATE IGNORE team_scores
SET weight = :value
WHERE team_name = :name
