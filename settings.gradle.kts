rootProject.name = "ts2kt-unofficial-gradle-plugin"

include(":plugin")
include(":functional-tests")

rootProject.children.forEach {
    it.name = "${rootProject.name}-${it.name}"
}
