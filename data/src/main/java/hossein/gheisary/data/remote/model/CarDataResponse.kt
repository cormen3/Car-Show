package hossein.gheisary.data.remote.model

class CarDataResponse constructor(
    var page:Int,
    var pageSize:Int,
    var totalPageCount:Int,
    var wkda:Map<String, String>)