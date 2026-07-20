package com.sigma.hospital.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.ai.GeminiApi;
import com.sigma.hospital.ai.GeminiClient;
import com.sigma.hospital.ai.GeminiRequest;
import com.sigma.hospital.ai.GeminiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiAssistantFragment extends Fragment {

    private TextInputEditText symptomsInput;
    private MaterialButton consultButton;
    private LinearLayout progressLayout;
    private CardView resultCard;
    private TextView responseText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("AI Health Companion");
        }

        symptomsInput = view.findViewById(R.id.ai_symptoms_input);
        consultButton = view.findViewById(R.id.btn_consult_ai);
        progressLayout = view.findViewById(R.id.ai_progress_layout);
        resultCard = view.findViewById(R.id.ai_result_card);
        responseText = view.findViewById(R.id.ai_response_text);

        consultButton.setOnClickListener(v -> executeAiConsultation());
    }

    private void executeAiConsultation() {
        String symptoms = symptomsInput.getText() != null ? symptomsInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(symptoms)) {
            symptomsInput.setError("Please describe symptoms first");
            return;
        }

        // Show loading state
        progressLayout.setVisibility(View.VISIBLE);
        resultCard.setVisibility(View.GONE);
        consultButton.setEnabled(false);

        String apiKey = GeminiClient.getApiKey();

        if (apiKey.isEmpty()) {
            // Graceful fallback offline simulation model
            simulateOfflineResponse(symptoms);
            return;
        }

        // Build Gemini payload request
        GeminiRequest request = new GeminiRequest(
                "You are an educational AI Medical Assistant for Sigma Hospital. " +
                "Review the following symptoms and output a professional, structured overview. " +
                "Include: " +
                "1. Possible general explanations (educational only, not a diagnostic verdict)\n" +
                "2. Specific hydration and lifestyle guidance\n" +
                "3. RED FLAG WARNINGS (When to immediately seek urgent care)\n" +
                "4. Assigned Specialty (Which medical department or doctor they should physically book a consultation with)\n\n" +
                "User Symptoms: " + symptoms
        );

        GeminiClient.getApi().generateContent(apiKey, request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeminiResponse> call, @NonNull Response<GeminiResponse> response) {
                if (isAdded() && getActivity() != null) {
                    progressLayout.setVisibility(View.GONE);
                    consultButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        String text = response.body().getText();
                        if (text != null && !text.isEmpty()) {
                            responseText.setText(text);
                            resultCard.setVisibility(View.VISIBLE);
                        } else {
                            simulateOfflineResponse(symptoms);
                        }
                    } else {
                        simulateOfflineResponse(symptoms);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeminiResponse> call, @NonNull Throwable t) {
                if (isAdded() && getActivity() != null) {
                    progressLayout.setVisibility(View.GONE);
                    consultButton.setEnabled(true);
                    simulateOfflineResponse(symptoms);
                }
            }
        });
    }

    private void simulateOfflineResponse(String symptoms) {
        // High fidelity offline-first local simulation guideline matching general user inputs
        String lower = symptoms.toLowerCase();
        String explanation, lifestyle, warnings, specialty;

        if (lower.contains("fever") || lower.contains("cough") || lower.contains("flu")) {
            explanation = "Your description points towards a general respiratory tract viral syndrome (such as Influenza or common rhinovirus).";
            lifestyle = "Increase fluid intake (hot tea, lemon, clear broths). Get absolute physical rest (8-10 hours). Avoid cold breezes.";
            warnings = "High fever exceeding 39°C unresponsive to paracetamol, respiratory distress, breathing shortness, or chest discomfort.";
            specialty = "General Medicine / Pulmonary Department. Please schedule a physical visit.";
        } else if (lower.contains("chest") || lower.contains("tight") || lower.contains("heart")) {
            explanation = "Sensation of chest tight/tightness can stem from muscular strain, acid reflux (GERD), anxiety hyperventilation, or active cardiac conditions.";
            lifestyle = "Avoid heavy physical exertion immediately. Practice calm deep breathing. Keep head elevated.";
            warnings = "Active radiation of pain to left arm or jaw, severe sweating, vomiting, or persistent crushing weight sensation.";
            specialty = "Cardiology Department. Seek urgent verification.";
        } else if (lower.contains("stomach") || lower.contains("pain") || lower.contains("diarrhea")) {
            explanation = "Symptoms suggest active gastroenteritis, indigestion, food toxicity, or general hyperacidity.";
            lifestyle = "Follow BRAT diet (Bananas, Rice, Applesauce, Toast). Ensure adequate electrolyte rehydration. Avoid spicy/greasy meals.";
            warnings = "Severe rebound tenderness in lower right abdominal quadrant, blood in stool, or inability to retain fluids for over 24 hours.";
            specialty = "Gastroenterology Department.";
        } else {
            explanation = "Your described complaints are processed. These are typical signs of fatigue, mild muscular inflammation, or general stress.";
            lifestyle = "Ensure balanced dietary meals. Maintain standard hydration levels (2-3 liters/day). Exercise moderate stretch routines.";
            warnings = "Unexplained sudden loss of consciousness, persistent numbness in limbs, or high fever with severe stiff neck.";
            specialty = "Internal Medicine / General Practitioner Clinic.";
        }

        String simulatedOutput = "SIGMA AI ASSISTANT (OFFLINE MODE)\n\n" +
                "--- 1. ANALYSIS SUMMARY ---\n" +
                explanation + "\n\n" +
                "--- 2. LIFESTYLE & HYDRATION GUIDES ---\n" +
                lifestyle + "\n\n" +
                "--- 3. CRITICAL RED FLAGS ---\n" +
                warnings + "\n\n" +
                "--- 4. CLINIC REFERRAL SPECIALTY ---\n" +
                specialty + "\n\n" +
                "NOTE: This offline diagnostic summary is an educational simulation based on pre-compiled clinical algorithms. Always physically verify diagnostic files.";

        // Toast alert for user awareness
        Toast.makeText(getContext(), "Offline Clinical Assistant Enabled", Toast.LENGTH_SHORT).show();

        responseText.setText(simulatedOutput);
        progressLayout.setVisibility(View.GONE);
        resultCard.setVisibility(View.VISIBLE);
        consultButton.setEnabled(true);
    }
}
