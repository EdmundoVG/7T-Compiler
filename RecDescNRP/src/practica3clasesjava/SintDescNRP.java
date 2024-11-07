/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3clasesjava;

/**
 *
 * @author Han-S
 */
public class SintDescNRP {

    public final int NODIS = 5000;

    private final  Pila _pila;
    //private final String[] _vts = {"", "id", "=", ";", "+", "-", "*", "/", "num", "(", ")", "$"};
    private final String[] _vts = {"Espacio", "Identificador", "Palabra Reservada", "Operador Relacional", "Operador Asignacion", "Operadores Dobles", "Operador Aritmetico", "Operador Logico", "Numero", "Separador", "Cadena", "Caracter", "Punto y Coma", "$"};
    
    private final String[] _vns = {"", "A", "E", "T", "F"};
    
    //La primera columna es el número de producción, la segunda es el No de Yes y los otros son tokens (posición en el arreglo, núm negativos)
    //y variables terminales (posición en el arreglo, núm positivo)
    private final int[][] _prod = {
    
    {1, 4, -1, -4, 2, -12, 0}, //A -> Identificador | Operador Asignacion | E | Punto y Coma
    {2, 2, 2, -5, 0, 0, 0},    //E -> E | Operadores Dobles
    {2, 3, 2, -6, 3, 0, 0},    //E -> E | Operador Aritmetico | T
    {2, 1, 3, 0, 0, 0, 0},     //E -> T
    {2, 0, 0, 0, 0, 0, 0},     //E -> $
    {3, 3, 3, -3, 4, 0, 0},    //T -> T | Operador Relacional | F
    {3, 2, -7, 4, 0, 0, 0},    //T -> Operador Logico | F 
    {3, 1, 4, 0, 0, 0, 0},     //T -> F
    {4, 1, -1, 0, 0, 0, 0},    //F -> Identificador
    {4, 1, -8, 0, 0, 0, 0},    //F -> Numero
    {4, 3, -9, 2, -9, 0, 0},   //F -> Separador | E | Separador
    {4, 1, -10, 0, 0, 0, 0},   //F -> Cadena 
    {4, 1, -11, 0, 0, 0, 0},   //F -> Caracter
    {4, 1, -2, 0, 0, 0, 0}     //F -> Palabras Reservadas
    
    
};
    
    //3 columnas: primera vns, la segunda los vts y la tercera el número de la producción
    private final int[][] _m = {
        
        //NO SUPE COMO HACER ESTOOOOOOOO
        
    {1, 1, 0},
    {2, 1, 1},
    {2, 11, 2},
    {3, 1, 3},
    {3, 8, 3},
    {3, 9, 3},
    {4, 4, 4},
    {4, 5, 5},
    {4, 3, 6},
    {4, 10, 6},
    {5, 1, 7},
    {5, 8, 7},
    {5, 9, 7},
    {6, 6, 8},
    {6, 7, 9},
    {6, 4, 10},
    {6, 5, 10},
    {6, 3, 10},
    {6, 10, 10},
    {7, 1, 11},
    {7, 8, 12},
    {7, 9, 13}
    };
    
    public void tienePesos() {
        for(int i=0; i<_noEnt; i++) {
            String simb = _vts[_m[i][1]];
            if(simb.equals("$")) {
                System.out.println("Tiene");
            }
        }
    }

    private final int _noVts;
    private final int _noVns;
    private final int _noProd;
    private final int _noEnt;
    private final int[] _di;
    private int _noDis;

    // Metodos 
    public SintDescNRP() // Constructor -----------------------
    {
        _pila = new Pila();
        _noVts = _vts.length;
        _noVns = _vns.length;
        _noProd = 14;
        _noEnt = 22;
        _di = new int[NODIS];
        _noDis = 0;
    }  // Fin del Constructor ---------------------------------------

    public void Inicia() // Constructor -----------------------
    {
        _pila.Inicia();
        _noDis = 0;
    }

    public int[][] Prod() {
        return _prod;
    }
    public int Analiza(Lexico oAnaLex) {
        //Apuntará al símbolo gramatical del tope de la pila
        SimbGram x; /*= new SimbGram("");*/
        //Apuntará al siguiente símbolo en w$
        String a;
        //Referencia al número de producción si necesitamos buscarlo en la tabla M
        int noProd;
        //Metemos el $
        _pila.Inicia();
        _pila.Push(new SimbGram("$"));
        //Mete el símbolo de inicio
        _pila.Push(new SimbGram(_vns[1]));
        //Mete el $ hasta el final de la tabla de reconocimiento del léxico
        //oAnaLex.Anade("$", "$");
        
        //ae=0, porque w$ es un string (arrelgo de caracteres)
        int ae = 0;
        do {
            //Símbolo gramátical en el tope de la pila, primero será A
            x = _pila.Tope();
            //Regresa el arreglo de tokens y agarramos el elemento 0
            a = oAnaLex.Tokens()[ae];
            //Método para ver si el símbolo gramátical al que apunta x es terminal o no
            //X es un objeto de la clase símbolo gramátical, x regresa un string
            if (EsTerminal(x.Elem())) {
                //Si X==a saco al tope de la pila e incremento ae
                if (x.Elem().equals(a)) {
                    _pila.Pop();
                    ae++;
                    //Error
                } else {
                    return 1;
                }
            } else {
                //Revisa si existe esa producción, pasando a 'X' y a 'a'
                if ((noProd = BusqProd(x.Elem(), a)) >= 0) {
                    //Sí existe, sacamos el tope de la pila
                    _pila.Pop();
                    //y metemos todas las yes de la anterior producción empezando desde el final
                    MeterYes(noProd);
                    //En derivación hacia la izquierda mete el no prod
                    _di[_noDis++] = noProd;
                } else {
                    return 2;
                }
            }
            //Mientras no sea igual a $
        } while (!x.Elem().equals("$"));
        return 0;
    }

    //Recibe un string y recorre el arrelgo de terminales, si lo encuentra regresa verdadero,
    //si no falso
    public boolean EsTerminal(String x) {
        for (int i = 1; i < _noVts; i++) {
            if (_vts[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    //Recibe dos strings
    public int BusqProd(String x, String a) {
        int indiceX = 0;
        //Busca el índice de x en las variables no terminales
        for (int i = 1; i < _noVns; i++) {
            if (_vns[i].equals(x)) {
                indiceX = i;
                break;
            }
        }
        ///Busca el índice de a en las variables terminales
        int indiceA = 0;
        for (int i = 1; i < _noVts; i++) {
            if (_vts[i].equals(a)) {
                indiceA = i;
                break;
            }
        }
        //Checamos si en las entradas de la tabla m existe la entrada
        for (int i = 0; i < _noEnt; i++) {
            //Revisa si existe alguna entrada en la tabla m que tenga ese vns y ese vns
            if (indiceX == _m[i][0] && indiceA == _m[i][1]) {
                //Regresa el indice de la producción
                return _m[i][2];
            }
        }
        //-1 si no existe
        return -1;
    }

    //Recibe el número de la producción
    public void MeterYes(int noProd) {
        //Obtenemos el número de yes para esa producción especifica
        int noYes = _prod[noProd][1];
        //Recorremos desde 1 hasta el número de yes
        for (int i = 1; i <= noYes; i++) {
            //Tomamos de prod el renglón y el último
            if (_prod[noProd][noYes + 2 - i] < 0) {
                //Si es menor que cero, tomamos el índice en vts, pero antes hay que hacerlo positivo
                _pila.Push(new SimbGram(_vts[-_prod[noProd][noYes + 2 - i]]));
                //Si es mayor que 0 quiere decir que es variable sintáctica
                //no hay necesidad de hacerlo positivo
            } else {
                _pila.Push(new SimbGram(_vns[_prod[noProd][noYes + 2 - i]]));
            }
        }
    }

    public String[] Vts() {
        return _vts;
    }

    public String[] Vns() {
        return _vns;

    }

    public int[][] M() {
        return _m;

    }

    public int NoDis() {
        return _noDis;
    }

    public int[] Di() {

        return _di;

    }

    public int IndiceVn(String vn) {
        for (int i = 1; i < _noVns; i++) {
            if (_vns[i].equals(vn)) {
                return i;
            }
        }
        return 0;
    }

    public int IndiceVt(String vt) {
        for (int i = 1; i < _noVts; i++) {
            if (_vts[i].equals(vt)) {
                return i;
            }
        }
        return 0;
    }

    public int NoProd() {
        return _noProd;
    }
    
    public String ProdCad(int noProd) {
        //[_prod[noProd][0]] Nos sirve para obtener el número de la variable sintactica en el arreglo _vns
        String aux=""+_vns[_prod[noProd][0]]+"->";
        int noYes = _prod[noProd][1];
        if(noYes>0) {
            for(int i=2; i<=noYes+1; i++) {
                if(_prod[noProd][i]<0) {
                    aux += " "+_vts[-_prod[noProd][i]];
                } else {
                    aux += " "+_vns[_prod[noProd][i]];
                }
            }
        } else {
            aux+=" £";
        }
        return aux;
    }
    
    public int noVts() {
        return _noVts;
    }
    
    public int noEnt() {
        return _noEnt;
    }
}
