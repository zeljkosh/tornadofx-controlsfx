
import impl.org.controlsfx.table.ColumnFilter
import javafx.beans.property.Property
import javafx.event.EventTarget
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.ToggleButton
import org.controlsfx.control.SegmentedButton
import org.controlsfx.control.ToggleSwitch
import org.controlsfx.control.table.TableFilter
import org.controlsfx.glyphfont.FontAwesome
import org.controlsfx.glyphfont.Glyph
import tornadofx.*


//TableFilter
private fun <T> TableView<T>.applyTableFilter(lazy: Boolean = true): TableFilter<T> {

    val tableFilter = TableFilter.forTableView(this)
            .lazy(lazy)
            .apply()

    this.properties.put("TableFilter",tableFilter)

    return tableFilter
}
fun <T> TableView<T>.tablefilter(op: (TableFilter<T>).() -> Unit = {}): TableFilter<T> {
    val tf = tableFilter
    tf.op()
    return tf
}

@Suppress("UNCHECKED_CAST")
val <T> TableView<T>.tableFilter: TableFilter<T> get() =  (properties["TableFilter"] as TableFilter<T>?)?:applyTableFilter()

@Suppress("UNCHECKED_CAST")
val <T,C> TableColumn<T,C>.columnFilter: ColumnFilter<T, C> get() =
    (tableView.tableFilter.getColumnFilter(this)
        .orElseThrow { Exception("TableFilter not initialized!") } as ColumnFilter<T, C>)
        .apply {
            initialize()
        }

fun <T,C> TableColumn<T,C>.columnfilter(op: ColumnFilter<T,C>.() -> Unit) {
    columnFilter.op()
}

fun <T> ColumnFilter<T,*>.selectValues(vararg values: Any?) {
    values.forEach { selectValue(it) }
}

fun <T> ColumnFilter<T,*>.exceptValue(value: Any?) {
    unSelectAllValues()
    selectValue(value)
}
fun <T> ColumnFilter<T,*>.exceptValues(vararg values: Any?) {
    unSelectAllValues()
    values.forEach { selectValue(it) }
}

private val fontAwesome by lazy {
    FontAwesome("http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/fonts/fontawesome-webfont.ttf")
}

fun FontAwesome.Glyph.toGlyph(op: (Glyph.() -> Unit)? = null): Glyph {
    val glyph = fontAwesome.create(this)
    op?.invoke(glyph)
    return glyph
}

//ToggleSwitch

fun EventTarget.toggleswitch(text: String? = null, selectedProperty: Property<Boolean>? = null, op: (ToggleSwitch.() -> Unit) = {}): ToggleSwitch {
    val toggleSwitch = ToggleSwitch(text)
    toggleSwitch.selectedProperty().bindBidirectional(selectedProperty)
    return opcr(this,toggleSwitch,op)
}

//SegmentedButton

fun EventTarget.segmentedbutton(op: (SegmentedButton.() -> Unit) = {}): SegmentedButton {
    val segmentedButton = SegmentedButton()

    return opcr(this,segmentedButton,op)
}

operator fun SegmentedButton.plusAssign(toggleButton: ToggleButton) {
    buttons.add(toggleButton)
}
fun SegmentedButton.button(text: String? = null, op: (ToggleButton.() -> Unit) = {}): ToggleButton {
    val toggleButton = ToggleButton(text)
    toggleButton.op()
    this += toggleButton
    return toggleButton
}