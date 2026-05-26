package com.example.demo.model;

public class AdminResponse {
    private long totalUsers, totalRides, totalPayments, totalVehicles,
                 totalParticipations, totalSchedules, totalRatings,
                 pendingRides, activeRides, completedRides, cancelledRides,
                 unreadNotifications, activeSos;
    private double totalCarbonSaved, totalRevenue;

    public long getTotalUsers()           { return totalUsers; }
    public void setTotalUsers(long v)     { totalUsers = v; }
    public long getTotalRides()           { return totalRides; }
    public void setTotalRides(long v)     { totalRides = v; }
    public long getTotalPayments()        { return totalPayments; }
    public void setTotalPayments(long v)  { totalPayments = v; }
    public long getTotalVehicles()        { return totalVehicles; }
    public void setTotalVehicles(long v)  { totalVehicles = v; }
    public long getTotalParticipations()  { return totalParticipations; }
    public void setTotalParticipations(long v) { totalParticipations = v; }
    public long getTotalSchedules()       { return totalSchedules; }
    public void setTotalSchedules(long v) { totalSchedules = v; }
    public long getTotalRatings()         { return totalRatings; }
    public void setTotalRatings(long v)   { totalRatings = v; }
    public long getPendingRides()         { return pendingRides; }
    public void setPendingRides(long v)   { pendingRides = v; }
    public long getActiveRides()          { return activeRides; }
    public void setActiveRides(long v)    { activeRides = v; }
    public long getCompletedRides()       { return completedRides; }
    public void setCompletedRides(long v) { completedRides = v; }
    public long getCancelledRides()       { return cancelledRides; }
    public void setCancelledRides(long v) { cancelledRides = v; }
    public long getUnreadNotifications()  { return unreadNotifications; }
    public void setUnreadNotifications(long v) { unreadNotifications = v; }
    public long getActiveSos()            { return activeSos; }
    public void setActiveSos(long v)      { activeSos = v; }
    public double getTotalCarbonSaved()   { return totalCarbonSaved; }
    public void setTotalCarbonSaved(double v) { totalCarbonSaved = v; }
    public double getTotalRevenue()       { return totalRevenue; }
    public void setTotalRevenue(double v) { totalRevenue = v; }
}
