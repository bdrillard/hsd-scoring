UPDATE IGNORE team_scores
SET school_name = :value
WHERE team_name = :name
