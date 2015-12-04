package src;


import java.util.Scanner;

public class EntradaDados {
	private int varObj, linhas, colunas , colunasZ , simplex ;
	private float[][] A;
	private String[] restricao;
	private float[] b , c;
	private boolean exibirIteracoes;
	private boolean DuasFases = false;
	public EntradaDados(){
		Scanner reader = new Scanner(System.in);
		System.out.println("Deseja imprimir todas as iterções do simplex? (s/n)");
		String respostaIter=reader.next();
		exibirIteracoes(respostaIter);
		System.out.println("Qual simplex deseja executar ?");
		System.out.println("1- Inserir um novo simplex de duas fases");
		System.out.println("2- Exemplo do caderno : Simplex duas fases");
		System.out.println("3- Simplex do trabalho :XYZ Sawmill Company");

		
		int resposta= reader.nextInt();
		checarResposta(resposta);
	}
	private void exibirIteracoes(String respostaIter) {
		if(respostaIter.equals("s")){
			setExibirIteracoes(true);
		}else{
			setExibirIteracoes(false);
		}
	}
	private void checarResposta(int resposta) {
		switch (resposta) {
		case 1:
			leDados(new Scanner(System.in));
			setDuasFases(true);
			setSimplex(1);
			break;
		case 2:
			simplexDuasFasesCaderno();
			setDuasFases(true);

			setSimplex(2);
			break;
		case 3:
			simplexTrabalho();
			setDuasFases(true);
			setSimplex(3);
			break;

		default:
			break;
		}
	}
	private void teste3() {
		setLinhas(2);
		setColunas(2);
		setColunasZ(getColunas());
		
		double[][] ai = {
	            {  1.0, 1.0 },
	            {  1.0, 2.0 }
	        };
		setA(new float[getLinhas()][getColunas()]);
		getA()[0][0] = 1;
		getA()[0][1] = 1;
		getA()[1][0] = 1;
		getA()[1][1] = 2;
		
		setRestricao(new String[getLinhas()]);
		getRestricao()[0] = "<=";
		getRestricao()[1] = "<=";
		
		float[] ci = {  -1,  -2 };
		setC(ci);;
		float[] bi = { 6, 10 };
        setB(bi);
        
        
    }
	private void simplexTrabalho() {
		setLinhas(6);
		setColunas(9);
		setColunasZ(getColunas());
		setA(new float[getLinhas()][getColunas()]);
		getA()[0][0] = 1;
		getA()[0][1] = 1;
		getA()[0][2] = 1;
		getA()[0][3] = 0;
		getA()[0][4] = 0;
		getA()[0][5] = 0;
		getA()[0][6] = 0;
		getA()[0][7] = 0;
		getA()[0][8] = 0;
		
		getA()[1][0] = 0;
		getA()[1][1] = 0;
		getA()[1][2] = 0;
		getA()[1][3] = 1;
		getA()[1][4] = 1;
		getA()[1][5] = 1;
		getA()[1][6] = 0;
		getA()[1][7] = 0;
		getA()[1][8] = 0;
		
		getA()[2][0] = 0;
		getA()[2][1] = 0;
		getA()[2][2] = 0;
		getA()[2][3] = 0;
		getA()[2][4] = 0;
		getA()[2][5] = 0;
		getA()[2][6] = 1;
		getA()[2][7] = 1;
		getA()[2][8] = 1;
		
		getA()[3][0] = 1;
		getA()[3][1] = 0;
		getA()[3][2] = 0;
		getA()[3][3] = 1;
		getA()[3][4] = 0;
		getA()[3][5] = 0;
		getA()[3][6] = 1;
		getA()[3][7] = 0;
		getA()[3][8] = 0;
		
		getA()[4][0] = 0;
		getA()[4][1] = 1;
		getA()[4][2] = 0;
		getA()[4][3] = 0;
		getA()[4][4] = 1;
		getA()[4][5] = 0;
		getA()[4][6] = 0;
		getA()[4][7] = 1;
		getA()[4][8] = 0;

		getA()[5][0] = 0;
		getA()[5][1] = 0;				
		getA()[5][2] = 1;
		getA()[5][3] = 0;
		getA()[5][4] = 0;				
		getA()[5][5] = 1;
		getA()[5][6] = 0;
		getA()[5][7] = 0;				
		getA()[5][8] = 1;
		
		setRestricao(new String[getLinhas()]);
		getRestricao()[0] = ">=";
		getRestricao()[1] = ">=";
		getRestricao()[2] = ">=";
		getRestricao()[3] = "<=";
		getRestricao()[4] = "<=";
		getRestricao()[5] = "<=";
		
		setB(new float[getLinhas()]);
		getB()[0] = 30;
		getB()[1] = 35;
		getB()[2] = 30;
		getB()[3] = 20;
		getB()[4] = 30;
		getB()[5] = 45;
		
		setVarObj(9);
		setC(new float[getVarObj()]);
		getC()[0] = 32;
		getC()[1] = 40;
		getC()[2] =	120;
		getC()[3] =	60;	
		getC()[4] = 68;
		getC()[5] = 104;
		getC()[6] = 200;
		getC()[7] = 80;
		getC()[8] = 60;
		
	}
	private void simplexDuasFasesCaderno() {
		setLinhas(4);
		setColunas(2);
		setColunasZ(getColunas());		
		
		setA(new float[getLinhas()][getColunas()]);
		
		getA()[0][0] = 3;
		getA()[0][1] = 2;
		getA()[1][0] = 4;
		getA()[1][1] = 1;
		getA()[2][0] = -2;
		getA()[2][1] = 3;
		getA()[3][0] = 1;
		getA()[3][1] = 4;
		
		setRestricao(new String[getLinhas()]);

		getRestricao()[0] = ">=";
		getRestricao()[1] = "<=";
		getRestricao()[2] = "<=";
		getRestricao()[3] = ">=";
		
		setB(new float[getLinhas()]);
		getB()[0] = 6;
		getB()[1] = 16;
		getB()[2] = 6;
		getB()[3] = 4;
		
		setVarObj(2);
		setC(new float[getVarObj()]);
		getC()[0] = -1;
		getC()[1] = -1;
		
		
	}
	private void leDados(Scanner reader){
		System.out.println("Entre com o valor de m (numero de linhas da matriz A)");
		setLinhas(reader.nextInt());
		System.out.println("Entre com o valor de n(numero de colunas da matriz A");
		setColunas(reader.nextInt());
		setColunasZ(getColunas());
		System.out.println("Entre com os valores da matriz A");
		setA(new float[getLinhas()][getColunas()]);
		for(int i=0;i<getLinhas();i++){
			for(int j=0;j<getColunas() ;j++){
				getA()[i][j] = reader.nextFloat();
			}
		}
		System.out.println("Entre com o tipo de restrição de cada linha (>= , <= ou =)");
		setRestricao(new String[getLinhas()]);
		reader.nextLine();
		for(int i=0;i<getLinhas();i++){
			 getRestricao()[i]= 	reader.nextLine();
				System.out.println(i + " " + getRestricao()[i]);
		}
		System.out.println("Entre com os valores de b");
		
		setB(new float[getLinhas()]);
		
		for(int j=0;j<getLinhas();j++){
			getB()[j] =  reader.nextFloat();
			if(getB()[j] < 0){System.out.println("Valor errado. Terminando programa.");	reader.close();	return;		}
		}
		System.out.println("Entre com o numero de variaveis da função objetivo");
		setVarObj(reader.nextInt());
		System.out.println("Entre com os valores de c");
		setC(new float[getVarObj()]);
		for (int i=0 ; i<getVarObj() ; i++){
			getC()[i] = reader.nextFloat();
		}
	
		
		reader.close();
	}
	public int getLinhas() {
		return linhas;
	}
	public void setLinhas(int linhas) {
		this.linhas = linhas;
	}
	public int getColunas() {
		return colunas;
	}
	public void setColunas(int colunas) {
		this.colunas = colunas;
	}
	public int getColunasZ() {
		return colunasZ;
	}
	public void setColunasZ(int colunasZ) {
		this.colunasZ = colunasZ;
	}
	public float[][] getA() {
		return A;
	}
	public void setA(float[][] a) {
		A = a;
	}
	public String[] getRestricao() {
		return restricao;
	}
	public void setRestricao(String[] restricao) {
		this.restricao = restricao;
	}
	public float[] getB() {
		return b;
	}
	public void setB(float[] b) {
		this.b = b;
	}
	public int getVarObj() {
		return varObj;
	}
	public void setVarObj(int varObj) {
		this.varObj = varObj;
	}
	public float[] getC() {
		return c;
	}
	public void setC(float[] c) {
		this.c = c;
	}
	public boolean isExibirIteracoes() {
		return exibirIteracoes;
	}
	public void setExibirIteracoes(boolean exibirIteracoes) {
		this.exibirIteracoes = exibirIteracoes;
	}
	public int getSimplex() {
		return simplex;
	}
	public void setSimplex(int simplex) {
		this.simplex = simplex;
	}
	public boolean isDuasFases() {
		return DuasFases;
	}
	public void setDuasFases(boolean duasFases) {
		DuasFases = duasFases;
	}

}
