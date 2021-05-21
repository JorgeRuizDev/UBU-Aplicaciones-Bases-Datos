package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TipoIncidenciaRanking
 */


//@Entity
public class TipoIncidenciaRanking implements Serializable {

	private static final long serialVersionUID = 1L;


	public TipoIncidenciaRanking() {
		super();
	}

	public TipoIncidenciaRanking(String tipo, int penalizacion) {
		if (! TiposIncidencias.contains(tipo, penalizacion)){
			throw new RuntimeException("Tipo de Incidencia Incorrecto");
		}

	}

	private enum TiposIncidencias {
		MUY_GRAVE("Muy grave", 12),
		GRAVE("Grave", 6),
		MODERADA("Moderada", 3),
		LEVE("Leve", 1);

		private final String tipo;
		private final int valor;

		TiposIncidencias(String tipo, int valor) {
			this.tipo = tipo;
			this.valor = valor;
		}

		public String getTipo() {
			return tipo;
		}

		public int getValor() {
			return valor;
		}

		public static boolean contains(String tipo, int valor) {
			for (TiposIncidencias incidencia : TiposIncidencias.values()) {
				if (incidencia.getTipo().equals(tipo))
					return true;
			}
			return false;
		}
	}

}
