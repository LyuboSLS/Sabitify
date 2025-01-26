package hr.algebra.sabitify.dao


private const val DB_NAME = "Sabitify.db"
private const val DB_VERSION = 1
/*
private var ITEM_TABLE_NAME = Item::class.java.name.toString()
private var VENUE_TABLE_NAME = Venue::class.java.name.toString()
private var EVENT_DATE_TABLE_NAME = EventDate::class.java.name.toString()
private var EVENT_LOCATION_TABLE_NAME = EventLocation::class.java.name.toString()
private var TICKETINFO_TABLE_NAME = TicketInfo::class.java.name.toString()
private var ITEM_TICKETINFO_TABLE_NAME =
    TicketInfo::class.java.name.toString() +
            Item::class.java.name.toString()+
            "JoinTable"


private val CREATE_VENUE_TABLE = "create table $VENUE_TABLE_NAME( " +
        "${Venue::_id.name} integer primary key autoincrement, " +
        "${Venue::name.name} text not null, " +
        "${Venue::rating.name} real not null, " +
        "${Venue::reviews.name} integer not null, " +
        "${Venue::link.name} text not null" +
        ")"

private val CREATE_EVENT_DATE_TABLE = "create table $EVENT_DATE_TABLE_NAME( " +
        "${EventDate::_id.name} integer primary key autoincrement, " +
        "${EventDate::start_date.name} text not null" +
        ")"

private val CREATE_EVENT_LOCATION_TABLE = "create table $EVENT_LOCATION_TABLE_NAME( " +
        "${EventLocation::_id.name} integer primary key autoincrement, " +
        "${EventLocation::link.name} text not null" +
        ")"

private val CREATE_TICKETINFO_TABLE = "create table $TICKETINFO_TABLE_NAME( " +
        "${TicketInfo::_id.name} integer primary key autoincrement, " +
        "${TicketInfo::source.name} text not null, " +
        "${TicketInfo::link.name} text not null, " +
        "${TicketInfo::link_type.name} text not null" +
        ")"

private val CREATE_ITEM_TABLE = "create table $ITEM_TABLE_NAME( " +
        "${Item::_id.name} integer primary key autoincrement, " +
        "${Item::title.name} text not null, " +
        "${Item::date.name} integer not null, " +  // Foreign key to EventDate
        "${Item::address.name} text not null, " +  // Stored as JSON or serialized
        "${Item::link.name} text not null, " +
        "${Item::eventLocationMap.name} integer not null, " +  // Foreign key to EventLocation
        "${Item::description.name} text not null, " +
        "${Item::thumbnail.name} text, " +
        "${Item::image.name} text, " +
        "${Item::read.name} integer not null, " +
        "${Item::venue.name} integer not null, " +  // Foreign key to Venue
        "foreign key (${Item::date.name}) references $EVENT_DATE_TABLE_NAME(${EventDate::_id.name}), " +
        "foreign key (${Item::eventLocationMap.name}) references $EVENT_LOCATION_TABLE_NAME(${EventLocation::_id.name}), " +
        "foreign key (${Item::venue.name}) references $VENUE_TABLE_NAME(${Venue::_id.name})" +
        ")"
/*
private val CREATE_ITEM_TICKETINFO_TABLE = "create table $ITEM_TICKETINFO_TABLE_NAME( " +
        "item_id integer not null, " +  // Foreign key to Item
        "ticket_info_id integer not null, " +  // Foreign key to TicketInfo
        "primary key (item_id, ticket_info_id), " +
        "foreign key (item_id) references $ITEM_TABLE_NAME(${Item::_id.name}), " +
        "foreign key (ticket_info_id) references $TICKETINFO_TABLE_NAME(${TicketInfo::id.name})" +
        ")"

private var DROP_TABLE = "drop table $ITEM_TABLE_NAME"

class SabitifySqlHelper: SQLiteOpenHelper(
    context,

) {


}*/