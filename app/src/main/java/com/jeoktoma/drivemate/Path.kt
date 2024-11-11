package com.jeoktoma.drivemate

data class RouteRequest(
    val start_location: Point,
    val end_location: Point
)
data class RouteResponse(
    val totalDistance: Int,
    val totalTime: Int,
    val route: Route
)

data class Route(
    val segments: List<Segment>
)

data class Segment(
    val startPoint: Point,
    val endPoint: Point,
    val path: List<Point>,
    val distance: Int,
    val time: Double,
    val roadName: String,
    val traffic: String
)

data class Point(
    val lat: Double,
    val lng: Double
)

data class CoordRequest(val road_address: String)
data class CoordResponse(val lat: Double, val lng: Double)


