package enums;

/** Tipos de unidad de la jerarquía de la biblioteca. */
public enum TipoUnidad {
    BIBLIOTECA, AREA, ESTANTE;

    /** Indica si una unidad de este tipo puede tener como hijo una unidad del tipo dado. */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) {
            return false;
        }
        switch (this) {
            case BIBLIOTECA: return hijo == AREA;
            case AREA:       return hijo == ESTANTE;
            default:         return false; // un ESTANTE no tiene hijos
        }
    }
}
