package fr.talium.mongodb.test.models

import org.squeryl.KeyedEntity
import DB._

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 14/06/13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
case class Product(
    id: Long,
    name: String
) extends KeyedEntity[Long] {
    lazy val categories = productsToCategories.left(this)
}
