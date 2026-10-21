package com.example.practicalfive;

/**
 * A plain Java helper class (not an Activity) that validates and normalises
 * the municipality name typed by the user.
 *
 * It accepts the four direct-administered municipalities of China
 * (Beijing, Shanghai, Chongqing and Tianjin) in any letter case and returns
 * the correctly capitalised name. Any other input - including null, empty
 * or whitespace-only strings and unsupported city names - is treated as
 * invalid and produces {@code null}.
 */
public class TextProcessor {

    /** The four supported municipalities, correctly capitalised. */
    private static final String[] SUPPORTED_MUNICIPALITIES = {
            "Beijing", "Shanghai", "Chongqing", "Tianjin"
    };

    /**
     * Validates the given municipality name.
     *
     * @param municipality the raw text entered by the user
     * @return the correctly capitalised municipality name if the input is
     *         valid (case-insensitive), or {@code null} when the input is
     *         null, empty, whitespace-only or not a supported municipality
     */
    public String processMunicipality(String municipality) {
        // Null, empty or whitespace-only input is invalid.
        if (municipality == null) {
            return null;
        }
        String trimmed = municipality.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        // Case-insensitive comparison against the four supported names.
        for (String name : SUPPORTED_MUNICIPALITIES) {
            if (name.equalsIgnoreCase(trimmed)) {
                return name;
            }
        }

        // Unsupported names (for example "Guangzhou") are invalid.
        return null;
    }
}
