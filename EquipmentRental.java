class EquipmentRental {
    private User user;
    private Equipment equipment;
    private int duration;

    public EquipmentRental(User user, Equipment equipment, int duration) {
        this.user = user;
        this.equipment = equipment;
        this.duration = duration;
    }

    public User getUser() {
        return user;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getDuration() {
        return duration;
    }
}
