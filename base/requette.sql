-- montant des devis par mois et année
WITH all_months AS (
    SELECT DISTINCT
        generate_series(
            DATE_TRUNC('year', MIN(date_paiement)),
            DATE_TRUNC('month', MAX(date_paiement)),
                '1 month'::interval
        ) AS mois
    FROM paiement
)
SELECT
    TO_CHAR(m.mois, 'YYYY-MM') AS mois_annee,
    COALESCE(SUM(p.montant), 0) AS total_montant
FROM
    all_months m
LEFT JOIN
    paiement p ON DATE_TRUNC('month', p.date_paiement) = m.mois
WHERE

GROUP BY
    m.mois
ORDER BY
    m.mois;
