package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the TIPOINCIDENCIA database table.
 * 
 */
@Entity
@NamedQuery(name="TipoIncidencia.findAll", query="SELECT t FROM TipoIncidencia t")
public class TipoIncidencia implements Serializable {
	private static final long serialVersionUID = 1L;


	@Transient
	private static final int longDescripcion = 30;

	@Id
	@SequenceGenerator(name="TIPOINCIDENCIA_ID_GENERATOR", sequenceName="TIPOINCIDENCIA_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOINCIDENCIA_ID_GENERATOR")
	private long id;


	@Column(name = "descripcion", length=longDescripcion)
	private String descripcion;

	@Column(name="valor", precision=10, scale=0)
	private BigDecimal valor;

	//bi-directional many-to-one association to Incidencia
	@OneToMany(mappedBy="tipoIncidencia")
	private Set<Incidencia> incidencias;

	public TipoIncidencia() {
	}

	public TipoIncidencia(String descripcion, BigDecimal valor){
		this.descripcion = descripcion;
		this.valor = valor;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Set<Incidencia> getIncidencias() {
		return this.incidencias;
	}

	public void setIncidencias(Set<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}

	public Incidencia addIncidencia(Incidencia incidencia) {
		getIncidencias().add(incidencia);
		incidencia.setTipoIncidencia(this);

		return incidencia;
	}

	public Incidencia removeIncidencia(Incidencia incidencia) {
		getIncidencias().remove(incidencia);
		incidencia.setTipoIncidencia(null);

		return incidencia;
	}

	public static int getLongDescripcion() {
		return longDescripcion;
	}

	@Override
	public String toString() {
		return "TipoIncidencia{" +
				"id=" + id +
				", descripcion='" + descripcion + '\'' +
				", valor=" + valor +
				", incidencias=" + "_" +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TipoIncidencia)) return false;
		TipoIncidencia that = (TipoIncidencia) o;
		return id == that.id && Objects.equals(descripcion, that.descripcion) && Objects.equals(valor, that.valor) && Objects.equals(incidencias, that.incidencias);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, descripcion, valor, incidencias);
	}
}