package argus.conf

object Plain extends PersistenceConf {
  override def url: String = "localhost"

  override def port: Int = 6379
}