package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TipoIncidenciaRanking
 */


//@Entity
public class TipoIncidenciaRanking implements Serializable {

	private static final long serialVersionUID = 1L;

	String tipo;
	long nApariciones;

	public TipoIncidenciaRanking() {
		super();
	}

	public TipoIncidenciaRanking(String tipo, long nApariciones) {
		if (! TiposIncidencias.contains(tipo)){
			throw new RuntimeException("Tipo de Incidencia Incorrecto");
		}

		this.tipo = tipo;
		this.nApariciones = nApariciones;
	}

	private enum TiposIncidencias {
		MUY_GRAVE("Muy grave"),
		GRAVE("Grave"),
		MODERADA("Moderada"),
		LEVE("Leve");

		private final String tipo;

		TiposIncidencias(String tipo) {
			this.tipo = tipo;
		}

		public String getTipo() {
			return tipo;
		}


		public static boolean contains(String tipo) {
			for (TiposIncidencias incidencia : TiposIncidencias.values()) {
				if (incidencia.getTipo().equals(tipo))
					return true;
			}
			return false;
		}
	}

	@Override
	public String toString() {
		return "TipoIncidenciaRanking{" +
				"tipo='" + tipo + '\'' +
				", nApariciones=" + nApariciones +
				'}';
	}
}
