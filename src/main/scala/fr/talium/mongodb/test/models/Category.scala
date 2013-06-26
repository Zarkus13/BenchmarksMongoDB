package fr.talium.mongodb.test.models

import org.squeryl.annotations._
import org.squeryl.dsl.ManyToOne
import DB._
import org.squeryl.KeyedEntity

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 13/06/13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
case class Category(
    id: String,
    name: String,
    @Column("parent_fk") parentId: Option[String]
) extends KeyedEntity[String] {
    lazy val parent: ManyToOne[Category] = categoriesToSubCategories.right(this)

    lazy val products = productsToCategories.right(this)
}