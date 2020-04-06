package argus.conf

object Plain extends PersistenceConf {
  override def uri: String = "localhost"

  override def port: Int = 6379
}