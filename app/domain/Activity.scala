package domain

import java.util.Date

case class Activity(filename: String, count: Int, timestamp: Date)

case class RecentActivity(id: Int, name: String, timestamp: Date)