package com.example.lucky__bank.maper;
import com.example.lucky__bank.dto.JobApplicationDto;
import com.example.lucky__bank.model.JobApplication;
import com.example.lucky__bank.model.JobPosition;
import com.example.lucky__bank.service.Base64Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApplicationMapper {
    private final Base64Utils base64Utils;

    public JobApplicationDto toDto(JobApplication jobApplication) {
        JobApplicationDto dto = JobApplicationDto.builder()
                .id(jobApplication.getId())
                .firstName(jobApplication.getFirstName())
                .lastName(jobApplication.getLastName())
                .email(jobApplication.getEmail())
                .phone(jobApplication.getPhone())
                .dateOfBirth(jobApplication.getDateOfBirth())
                .educationLevel(jobApplication.getEducationLevel().name())
                .university(jobApplication.getUniversity())
                .degree(jobApplication.getDegree())
                .graduationYear(jobApplication.getGraduationYear())
                .currentPosition(jobApplication.getCurrentPosition())
                .currentCompany(jobApplication.getCurrentCompany())
                .workExperience(jobApplication.getWorkExperience())
                .skills(jobApplication.getSkills())
                .appliedPosition(jobApplication.getAppliedPosition().getPosition())
                .availableStartDate(jobApplication.getAvailableStartDate())
                .willingToRelocate(jobApplication.isWillingToRelocate())
                .additionalInfo(jobApplication.getAdditionalInfo())
                .applicationDate(jobApplication.getApplicationDate())
                .linkedinProfile(jobApplication.getLinkedinProfile())
                .agreedToTerms(jobApplication.isAgreedToTerms())
                .preferredContactMethod(jobApplication.getPreferredContactMethod().name())
                .build();

        if (jobApplication.getResume() != null) {
            dto.setResume(base64Utils.encodeBase64(jobApplication.getResume()));
        }

        return dto;
    }

    public JobApplication toEntity(JobApplicationDto jobApplicationDto, JobPosition jobPosition) {
        JobApplication jobApplication = JobApplication.builder()
                .id(jobApplicationDto.getId())
                .firstName(jobApplicationDto.getFirstName())
                .lastName(jobApplicationDto.getLastName())
                .email(jobApplicationDto.getEmail())
                .phone(jobApplicationDto.getPhone())
                .dateOfBirth(jobApplicationDto.getDateOfBirth())
                .educationLevel(JobApplication.EducationLevel.valueOf(jobApplicationDto.getEducationLevel()))
                .university(jobApplicationDto.getUniversity())
                .degree(jobApplicationDto.getDegree())
                .graduationYear(jobApplicationDto.getGraduationYear())
                .currentPosition(jobApplicationDto.getCurrentPosition())
                .currentCompany(jobApplicationDto.getCurrentCompany())
                .workExperience(jobApplicationDto.getWorkExperience())
                .skills(jobApplicationDto.getSkills())
                .appliedPosition(jobPosition)
                .availableStartDate(jobApplicationDto.getAvailableStartDate())
                .willingToRelocate(jobApplicationDto.isWillingToRelocate())
                .additionalInfo(jobApplicationDto.getAdditionalInfo())
                .applicationDate(jobApplicationDto.getApplicationDate())
                .linkedinProfile(jobApplicationDto.getLinkedinProfile())
                .agreedToTerms(jobApplicationDto.isAgreedToTerms())
                .preferredContactMethod(JobApplication.PreferredContactMethod.valueOf(jobApplicationDto.getPreferredContactMethod()))
                .build();

        if (jobApplicationDto.getResume() != null) {
            jobApplication.setResume(base64Utils.decodeBase64(jobApplicationDto.getResume()));
        }

        return jobApplication;
    }
}