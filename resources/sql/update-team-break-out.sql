UPDATE IGNORE team_scores
SET break_out_score = :value
WHERE team_name = :name
