package pvtitov.timetable.database;

public enum ToOrFrom {
    TO ("citiesTo", StationsContract.Entry.TABLE_CITIES_TO),
    FROM ("citiesFrom", StationsContract.Entry.TABLE_CITIES_FROM);

    ToOrFrom(String direction, String table) {
        this.direction = direction;
        this.table = table;
    }

    private String direction;
    private String table;

    public String getDirection() {
        return direction;
    }

    public String getTable() {
        return table;
    }
}
