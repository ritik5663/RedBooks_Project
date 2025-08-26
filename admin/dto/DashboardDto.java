package com.redbooks.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDto {
    private Long totalSchools;
    private Long pendingSchools;
    private Long totalPublishers;
    private Long pendingPublishers;
    private Long totalUsers;
    private Long pendingRequests;
}
