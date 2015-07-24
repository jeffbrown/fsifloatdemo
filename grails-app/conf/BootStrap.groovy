class BootStrap {

    def init = { servletContext ->
        [4.5, 2.03, 21.12].each {
            new fsi.Bottle(volume: it).save()
        }
    }
}
