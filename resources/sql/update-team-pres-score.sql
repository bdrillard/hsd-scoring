UPDATE IGNORE team_scores
SET presentation_score = :value
WHERE team_name = :name
