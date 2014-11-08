UPDATE IGNORE team_scores
SET launch_score = :value
WHERE team_name = :name
