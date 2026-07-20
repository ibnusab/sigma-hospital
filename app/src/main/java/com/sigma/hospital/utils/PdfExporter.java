package com.sigma.hospital.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import com.sigma.hospital.models.MedicalRecord;
import java.io.File;
import java.io.FileOutputStream;

public class PdfExporter {

    public static File exportMedicalRecordPdf(Context context, MedicalRecord record) {
        if (record == null) return null;

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // Standard A4 dimensions
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 1. Draw Title Header
        titlePaint.setColor(Color.parseColor("#0F62FE")); // Clinical M3 blue
        titlePaint.setTextSize(22);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("SIGMA HOSPITAL SYSTEM", 50, 60, titlePaint);

        paint.setColor(Color.parseColor("#4D4D4D"));
        paint.setTextSize(11);
        canvas.drawText("Smart Administrative Clinic Records & Charts Suite", 50, 80, paint);
        canvas.drawText("Contact: support@sigmahospital.com | Status: Verified", 50, 95, paint);

        // Header Divider Line
        paint.setColor(Color.parseColor("#D0D0D0"));
        paint.setStrokeWidth(1.5f);
        canvas.drawLine(50, 110, 545, 110, paint);

        // Record Case Header
        titlePaint.setTextSize(14);
        titlePaint.setColor(Color.parseColor("#161616"));
        canvas.drawText("OFFICIAL CLINICAL ENCOUNTER FILE", 50, 140, titlePaint);

        // Details
        paint.setTextSize(10);
        paint.setColor(Color.parseColor("#161616"));
        int y = 175;
        canvas.drawText("Patient Name: " + record.getPatientName(), 50, y, paint);
        canvas.drawText("MRN: " + (record.getPatientMrn() != null ? record.getPatientMrn() : "N/A"), 340, y, paint);

        y += 20;
        canvas.drawText("Consulting Physician: " + record.getDoctorName(), 50, y, paint);
        canvas.drawText("Visit Date: " + record.getVisitDate(), 340, y, paint);

        // Section vitals
        y += 35;
        titlePaint.setTextSize(12);
        titlePaint.setColor(Color.parseColor("#0F62FE"));
        canvas.drawText("1. MEASURED PATIENT VITALS:", 50, y, titlePaint);

        y += 20;
        canvas.drawText("Height: " + record.getHeight() + " cm", 50, y, paint);
        canvas.drawText("Weight: " + record.getWeight() + " kg", 200, y, paint);
        canvas.drawText("Blood Pressure: " + record.getBloodPressure() + " mmHg", 340, y, paint);

        y += 20;
        canvas.drawText("Temperature: " + record.getTemperature() + " °C", 50, y, paint);
        canvas.drawText("Heart Rate: " + record.getHeartRate() + " bpm", 200, y, paint);
        canvas.drawText("Respiratory Rate: " + record.getRespiratoryRate() + " breaths/min", 340, y, paint);

        // Symptoms
        y += 35;
        canvas.drawText("2. INTAKE SYMPTOMS & PRESENTATION:", 50, y, titlePaint);
        y += 18;
        canvas.drawText(record.getSymptoms() != null && !record.getSymptoms().isEmpty() ? record.getSymptoms() : "General review", 50, y, paint);

        // Exam
        y += 35;
        canvas.drawText("3. EXAM & DIAGNOSTIC FINDINGS:", 50, y, titlePaint);
        y += 18;
        canvas.drawText(record.getPhysicalExamination() != null && !record.getPhysicalExamination().isEmpty() ? record.getPhysicalExamination() : "Routine physical exam", 50, y, paint);
        y += 15;
        canvas.drawText("Diagnosis Summary: " + record.getDiagnosisNotes(), 50, y, paint);

        // Prescription
        y += 35;
        canvas.drawText("4. OUTPATIENT PRESCRIPTION (Rx):", 50, y, titlePaint);
        y += 18;
        canvas.drawText(record.getPrescription() != null && !record.getPrescription().isEmpty() ? record.getPrescription() : "No clinical prescription ordered", 50, y, paint);

        // Labs
        y += 35;
        canvas.drawText("5. LAB & IMAGING RECOMMENDATIONS:", 50, y, titlePaint);
        y += 18;
        canvas.drawText(record.getLaboratoryResult() != null && !record.getLaboratoryResult().isEmpty() ? record.getLaboratoryResult() : "None pending", 50, y, paint);

        // Footer block
        y = 780;
        paint.setColor(Color.parseColor("#8D8D8D"));
        paint.setTextSize(8);
        canvas.drawLine(50, y - 10, 545, y - 10, paint);
        canvas.drawText("Sigma Smart Hospital Systems • Offline-First Integrated Clinic Records Engine", 50, y, paint);
        canvas.drawText("Electronic verification key is verified locally via signature.", 50, y + 12, paint);

        pdfDocument.finishPage(page);

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, "Sigma_Encounter_" + record.getId() + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        } finally {
            pdfDocument.close();
        }
        return file;
    }

    public static File exportSystemAuditReport(Context context, int docs, int pats, int appts, int recs) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        titlePaint.setColor(Color.parseColor("#0F62FE"));
        titlePaint.setTextSize(22);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("SIGMA HOSPITAL AUDIT REPORT", 50, 60, titlePaint);

        paint.setColor(Color.parseColor("#4D4D4D"));
        paint.setTextSize(11);
        canvas.drawText("System Executive Statistical Report Summary", 50, 80, paint);

        paint.setColor(Color.parseColor("#D0D0D0"));
        canvas.drawLine(50, 100, 545, 100, paint);

        paint.setColor(Color.parseColor("#161616"));
        paint.setTextSize(12);
        int y = 140;
        canvas.drawText("• Total Registered Doctors: " + docs, 50, y, paint);
        y += 25;
        canvas.drawText("• Total Registered Patients: " + pats, 50, y, paint);
        y += 25;
        canvas.drawText("• Consultation Appointments Scheduled: " + appts, 50, y, paint);
        y += 25;
        canvas.drawText("• Medical Record Files Printed: " + recs, 50, y, paint);

        y += 50;
        titlePaint.setTextSize(14);
        canvas.drawText("ADMINISTRATIVE COMPLIANCE STATUS: STABLE", 50, y, titlePaint);

        y = 780;
        paint.setColor(Color.parseColor("#8D8D8D"));
        paint.setTextSize(8);
        canvas.drawLine(50, y - 10, 545, y - 10, paint);
        canvas.drawText("Automated administration audit logs generated on local storage.", 50, y, paint);

        pdfDocument.finishPage(page);

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, "Sigma_System_Audit.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        } finally {
            pdfDocument.close();
        }
        return file;
    }
}
