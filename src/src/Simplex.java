package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Simplex {
	private float  RHS =0;
	private float[] c;
	private float[] zFase1;
	private float[] saveColunaRHS;
	private float[][] tabela;
	private boolean exibirIteracoes = false;
	private boolean duasFases = false;

	 ArrayList<Integer> varBasicasAtuais = new ArrayList<Integer>();
	 ArrayList<Integer> varArtificiaisAtuais =new ArrayList<Integer>();
	 public Simplex(EntradaDados entrada){
			super();
			//Guarda os valores da função objetivo original
			this.setC(entrada.getC());
			this.setDuasFases(isDuasFases());
			//Define se irá exibir as iterações do simplex ou não, por padrão a ultima sempre irá ser impressa
			this.setExibirIteracoes(entrada.isExibirIteracoes());
			if(entrada.isDuasFases()){
				//Cria as variaveis extras necessárias para a formação da tabela da fase 1 do simplex de duas fases
				setTabela(criaVariaveisFase1(entrada.getA(), entrada.getRestricao(), entrada.getColunasZ(), entrada.getB()));
				//Junta a tabela da variaveis com os custos adaptados para a fase 1, RHS e vetor dos resultados para formar a tabela
				setTabela(montaTabelaFase1(getTabela(), entrada.getB()));
				//Executa a primeira fase do simplex de duas fases 
				executaFase1(getTabela());
				//Prepara o simplex para a execução da segunda fase(retira as variaveis artificiais)
				prepararFase2(getTabela());
				//Corrige as variaveis da linha Z para a fase 2
				corrigirLinhaZ(getTabela());
			}
			//Executa a fase 2 do simplex
			executaFase2(getTabela());
			if(!isExibirIteracoes()){setExibirIteracoes(true);
			printA(getTabela().length, getTabela()[0].length, getTabela());}
			//printSolucao(getTabela());
		
	 }
private void printSolucao(float[][] tabela2) {
			if(!isExibirIteracoes()){setExibirIteracoes(true);
			printA(getTabela().length, getTabela()[0].length, getTabela());}
	
			System.out.println("Valor da função objetivo = " + tabela2[0][tabela2[0].length-1]);
				for(int j=0;j<tabela2[0].length - varBasicasAtuais.size()-1 ; j++){
					if(tabela2[0][j] !=0){
					System.out.println("custo reduzido da variavel X["+(j+1)+"] = " + (-tabela2[0][j]));
					}else {
						System.out.println("custo reduzido da variavel X["+(j+1)+"] = " + (tabela2[0][j]));
						
					}
				}
				for(int j=tabela2[0].length - varBasicasAtuais.size()-1;j<tabela2[0].length-1; j++){
					System.out.println("resultado dual row: "+(j- varBasicasAtuais.size()-1)+") = " + (tabela2[0][j]));
				}
			
		
	}
	public float[][] criaVariaveisFase1(float[][] A, String[] restricoes, int colunasZ , float[] B){
		A = criarVariaveisFolga(A.length,A[0].length , restricoes , A);
		montaZFase1(A.length, A[0].length, restricoes, colunasZ, A, B);
		if(exibirIteracoes){printZ(A[0].length);
		printA(A.length,A[0].length, A);}
		return A;
	}

	public float[][] criarVariaveisFolga(int linhas, int colunas, String[] restricao, float[][] A) { //TODO Falta separar e adicionar variaveis de folga e artificiais separadamente --- acho que assim da pra checar se tem a identidade no meio
		for(int i=0; i<linhas ; i++){ // adicionando as variaveis de folga negativas
			if(restricao[i].equals(">=")){
				A = addNegVariaveis(i, linhas , colunas , A);
				colunas= colunas +1 ;
			}
			else if(restricao[i].equals("<=") || restricao[i].equals("=")){ //adicionando outras variaveis de folga
				A = add1variavel(i, linhas , colunas , A);
				colunas= colunas+1;

			}
			else{
				System.out.println("Restrição zoada");
			}
		}
		//adicionando as variaveis artificiais para termos matriz identidade
		for(int i=0; i<linhas ; i++){ 
			if(restricao[i].equals(">=")){
				A = criarVariaveisArtificiais(i , linhas , colunas , A);
				colunas = colunas +1;
			}
		}
		//montaZFase1();
		return A;
	}

	private float[][] criarVariaveisArtificiais(int linhaAtual, int linhas , int colunas, float[][] A) {
		
		float newA[][] = new float[linhas][colunas+1]; 
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas+1 ;y++){
				newA[x][y] = 0;
			}
		}
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas ;y++){
				newA[x][y] = A[x][y] ;
			}
		}
		newA[linhaAtual][colunas] = 1;
		varArtificiaisAtuais.add(colunas);
		varBasicasAtuais.add(colunas);
		return newA;	}
	
	public float[][] add1variavel(int linhaAtual, int linhas , int colunas , float[][] A) {
		
		float newA[][] = new float[linhas][colunas+1];
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas+1 ;y++){
				newA[x][y] = 0;
			}
		}
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas ;y++){
				newA[x][y] = A[x][y] ;
			}
		}
		newA[linhaAtual][colunas] = 1;
		varBasicasAtuais.add(colunas);
		return newA;
	}

	private float[][] addNegVariaveis(int linhaAtual,int linhas , int colunas , float[][] A) {
		float newA[][] = new float[linhas][colunas+1];
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas+1 ;y++){
				newA[x][y] = 0;
			}
		}
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas ;y++){
				newA[x][y] = A[x][y] ;
			}
		}
		newA[linhaAtual][colunas] = -1;
		return newA;
	}
	
	public void printA(int linhas , int colunas , float[][] A){
		if(isExibirIteracoes()){	for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas ;y++){
				System.out.print(" " + A[x][y]+ " ");
			}
			System.out.println("");
		}
		//System.out.println("Primeira linha = Z");
		//System.out.println(varBasicasAtuais + " basicas");
		//System.out.println(varArtificiaisAtuais +  " artificiais");
		System.out.println("--------------------------------------------------------------------------");
	}}
	
	public void montaZFase1(int linhas, int colunas , String[] restricao, int colunasZ, float[][] A, float[] b) {
		setRHS(0);
		setzFase1(new float[colunas]);
		for(int j=0; j<linhas;j++){
			if(restricao[j].equals(">=")){
				for(int i=0;i<colunasZ ; i++){
					getzFase1()[i] = getzFase1()[i] + A[j][i] ;
					
				}
				
				for(int k=colunasZ;k<colunas;k++){
					if(A[j][k] == -1){
						getzFase1()[k] = -1;
					}
				}
				setRHS((getRHS() + b[j]));
			}
		}
	}
	
	private void printZ(int colunas) {
		for(int y=0;y<colunas ;y++){
			System.out.print(" " + getzFase1()[y]+ " ");
		}
		System.out.print(getRHS() + " ");
		System.out.println("");		
	}
	
	public float[][] montaTabelaFase1( float[][] A , float[] b) {
		int linhas = A.length; 
		int colunas = A[0].length;
		float[][] tabelaFase1 = new float[linhas+1][colunas+1];
		for(int x=0;x<colunas;x++){
			tabelaFase1[0][x] =getzFase1()[x];
		}
		tabelaFase1[0][colunas] = getRHS();
		for(int i=1;i<linhas+1;i++){
			for(int x=0;x<colunas; x++){
				tabelaFase1[i][x] = A[i-1][x];
			}
		tabelaFase1[i][colunas] = b[i-1];
		}
		linhas= linhas +1 ; 
		colunas = colunas+1 ; 

	//	System.out.println("printando tabela fase 1 completa");
		printA(tabelaFase1.length,tabelaFase1[0].length, tabelaFase1);
		return tabelaFase1;
	}

	public void executaFase1(float[][] tabelaFase1) {
		checaEntraSaiBaseFazPivotFase1(tabelaFase1);
		
	}
	
	private void checaEntraSaiBaseFazPivotFase1(float[][] tabelaFase1) {
			int colunas = tabelaFase1[0].length;
			int linhas =  tabelaFase1.length;
			float maiorZ = 0;
			float  menorFracao = 10000;
			int coluna = -1  , linha = -1,colunaSaindo =-1;
			for(int y=0;y<colunas -1 ;y++){
				if((tabelaFase1[0][y] > maiorZ) && !varBasicasAtuais.contains(y)){
					maiorZ=tabelaFase1[0][y];
					coluna = y; //coluna y entra na base
				}else {
						
 				}
			}
			
			for(int i=1;i<linhas;i++){
				if((tabelaFase1[i][coluna]) > 0){
					if((tabelaFase1[i][colunas-1]/tabelaFase1[i][coluna]) <menorFracao){					
						menorFracao = (tabelaFase1[i][colunas-1]/tabelaFase1[i][coluna]);
						linha = i; //linha da base que vai sair
					}
				}
			}
			
			for(int i=0;i<colunas-1 ; i++){
				if((tabelaFase1[linha][i] == 1) && varBasicasAtuais.contains(i)){// se for 1 e tiver nas basicas = coluna que vai sair da base
					colunaSaindo = i ;
				}	
			}
			
			tabelaFase1 = pivoteamento(coluna,linha,tabelaFase1);
			//System.out.println("imprimindo tabela dps do pivoteamento");
		//	System.out.println("coluna que vai entrar na base : "+ coluna + " coluna que vai sair da base : "+ colunaSaindo);
			varBasicasAtuais.remove(varBasicasAtuais.indexOf(colunaSaindo));
			varBasicasAtuais.add(coluna);
			
			printA(tabelaFase1.length,tabelaFase1[0].length, tabelaFase1);

			checaOtimoFase1(tabelaFase1);
			
	}
	
	private void checaOtimoFase1(float[][] tabelaFase1) {
		int colunas = tabelaFase1[0].length;
		float count =0 ;
		boolean otima =false;
		
		for(int y=0;y<colunas -1 ;y++){
			if(varArtificiaisAtuais.contains(y) && tabelaFase1[0][y] >=0){
				checaEntraSaiBaseFazPivotFase1(tabelaFase1);
			}else if(varArtificiaisAtuais.contains(y) && tabelaFase1[0][y] <0) {
				count++;
				if(count == varArtificiaisAtuais.size()){
					System.out.println("Tabela é ótima para fase um");
					setTabela(tabelaFase1);
					//otima=true;
					prepararFase2(tabelaFase1);
					System.exit(0);
				}
			}
		}
	}

	private float[][] pivoteamento(int coluna, int linha , float[][] tabelaFase1) {
		int linhas =  tabelaFase1.length;
		int colunas = tabelaFase1[0].length;
		float[][] novaTabela = new float[linhas][colunas];
		//toda a tabela menos a coluna e linha que vão entrar e sair da base
		for(int x=0;x<linhas;x++){
			for(int y=0;y<colunas;y++){
				if(x != linha && y != coluna){
					novaTabela[x][y] = tabelaFase1[x][y] - tabelaFase1[linha][y]*tabelaFase1[x][coluna]/tabelaFase1[linha][coluna];
				}
			}
		}
		//zerar coluna
		for(int i=0;i<linhas;i++){
			if(i!=linha){
				novaTabela[i][coluna] = 0;
			}
		}
		//botar 1 
		for(int i=0;i<colunas;i++){
			if(i !=coluna ){
				novaTabela[linha][i] = tabelaFase1[linha][i]/tabelaFase1[linha][coluna];
			}
			novaTabela[linha][coluna] = 1;
		}
		
		return novaTabela;
	}
	
	public void prepararFase2(float[][] tabelaOtimaFase1) {
		int linhas = tabelaOtimaFase1.length;
		int colunas = tabelaOtimaFase1[0].length;
		printA(tabelaOtimaFase1.length,tabelaOtimaFase1[0].length, tabelaOtimaFase1);

		float[][] tabelaSemArtificiais =  new float[linhas][colunas-varArtificiaisAtuais.size()];
		setSaveColunaRHS(new float[tabelaSemArtificiais.length]);
		System.out.println("Entrando preparar fase2");
		for(int x=0;x<linhas;x++){
			getSaveColunaRHS()[x] = tabelaOtimaFase1[x][tabelaOtimaFase1[0].length-1]; 
		}
		for(int x=0;x<linhas;x++){
			for(int y=0;y<tabelaSemArtificiais[0].length -1;y++){
				tabelaSemArtificiais[x][y] = tabelaOtimaFase1[x][y];
			}
			tabelaSemArtificiais[x][tabelaSemArtificiais[0].length-1] = getSaveColunaRHS()[x];
		}
		printA(tabelaSemArtificiais.length, tabelaSemArtificiais[0].length, tabelaSemArtificiais);
		colunas = tabelaSemArtificiais[0].length;
		linhas = tabelaSemArtificiais.length;
		tabelaSemArtificiais = insereVarObjEmZ(tabelaSemArtificiais);
		printA(tabelaSemArtificiais.length, tabelaSemArtificiais[0].length, tabelaSemArtificiais);
		setTabela(tabelaSemArtificiais);
		corrigirLinhaZ(tabelaSemArtificiais);
		
	}
	
	private void corrigirLinhaZ(float[][] tabela) {
			int coluna = -1, linha = -1 ;
			ArrayList<Integer> colunas = new ArrayList<Integer>();
			for(int i=0;i<tabela[0].length-1 ; i++){
				
				if(varBasicasAtuais.contains(i) && tabela[0][i] !=0){
					coluna = i;   //coluna do pivo
					colunas.add(i);
				}
			}
			for (Integer colunaBase : colunas) {
				for(int i=1;i < tabela.length;i++){
					if(tabela[i][colunaBase]==1){
						linha =i;
						tabela=pivoteamento(colunaBase, linha, tabela);
					}
				}
			}
			printA(tabela.length, tabela[0].length, tabela);	
			checaLinhaZ(tabela);
			setTabela(tabela);
			executaFase2(tabela);
	}
	
	private void checaLinhaZ(float[][] tabela) {
		int colunas = tabela[0].length;
		int count=0;
		for(int y=0;y<colunas -1 ;y++){
			if(varBasicasAtuais.contains(y) && tabela[0][y]!=0){
			//	System.out.println("Existe uma basica diferente de zero");
				corrigirLinhaZ(tabela);
			}else if(varBasicasAtuais.contains(y) && tabela[0][y] == 0){
				count++;
			}else{
				
			}
		}
		if(count == varBasicasAtuais.size()){
			//System.out.println("Todas as basicas = 0");
		}
	}

	private float[][] insereVarObjEmZ(float[][] tabelaSemArtificiais) {
		for(int i=0;i<getC().length ; i++){
			tabelaSemArtificiais[0][i] =- getC()[i];
		}
		return tabelaSemArtificiais;
	}

	public void executaFase2(float[][] tabela) {
		System.out.println("Tabela esta pronta para fase 2");
		printA(tabela.length, tabela[0].length, tabela);
		tabela  = checaEntraSaiBaseFazPivotFase2(tabela);
	}
	

	private float[][] checaEntraSaiBaseFazPivotFase2(float[][] tabelaFase2) {
		int colunas =tabelaFase2[0].length;
		float maiorZ = -1;
		float  menorFracao = 10000;
		int coluna = -1  , linha = -1,colunaSaindo =-1;
		for(int y=0;y<tabelaFase2[0].length -1 ;y++){
			if(tabelaFase2[0][y] > maiorZ){
				maiorZ=tabelaFase2[0][y];
				coluna = y; //coluna y entra na base
			}else {
					
				}
		}
		for(int i=1;i<tabelaFase2.length;i++){
			if(!((tabelaFase2[i][coluna]) < 0)){
				if((tabelaFase2[i][tabelaFase2[0].length-1]/tabelaFase2[i][coluna]) <menorFracao){					
					menorFracao = (tabelaFase2[i][tabelaFase2[0].length-1]/tabelaFase2[i][coluna]);
					linha = i; //linha da base que vai sair
				}
			}
		}
		for(int i=0;i<colunas-1 ; i++){
			if((tabelaFase2[linha][i] == 1) && varBasicasAtuais.contains(i)){// se for 1 e tiver nas basicas = coluna que vai entrar na base
				colunaSaindo = i ;
			}
		}
	
		tabelaFase2 = pivoteamento(coluna,linha,tabelaFase2);
		//System.out.println("coluna que vai entrar na base : "+ coluna + " coluna que vai sair da base : "+ colunaSaindo);
		varBasicasAtuais.remove(varBasicasAtuais.indexOf(colunaSaindo));
		varBasicasAtuais.add(coluna);
		printA(tabelaFase2.length, tabelaFase2[0].length, tabelaFase2);
		setTabela(tabelaFase2);
		checaOtimoFase2(tabelaFase2);
		return tabelaFase2;
		
}
	
	private void checaOtimoFase2(float[][] tabelaFase2) {
	int colunas = tabelaFase2[0].length;
	int count = 0;
	for(int y=0;y<colunas -1 ;y++){
		if(tabelaFase2[0][y] > 0.0 ){
			//checaEntraSaiBaseFazPivotFase2(tabelaFase2);
			count++;
		}else {	
		}
	}
	if(count>0){
		checaEntraSaiBaseFazPivotFase2(tabelaFase2);
		
	}
	if(count == 0){
		setTabela(tabelaFase2);
		printSolucao(getTabela());
	}
}
public float getRHS() {
	return RHS;
}
public void setRHS(float f) {
	RHS = f;
}
public float[] getzFase1() {
	return zFase1;
}
public void setzFase1(float[] zFase1) {
	this.zFase1 = zFase1;
}
public float[][] getTabela() {
	return tabela;
}
public void setTabela(float[][] tabela) {
	this.tabela = tabela;
}
public float[] getC() {
	return c;
}
public void setC(float[] c) {
	this.c = c;
}
public float[] getSaveColunaRHS() {
	return saveColunaRHS;
}
public void setSaveColunaRHS(float[] saveColunaRHS) {
	this.saveColunaRHS = saveColunaRHS;
}
public boolean isExibirIteracoes() {
	return exibirIteracoes;
}
public void setExibirIteracoes(boolean exibirIteracoes) {
	this.exibirIteracoes = exibirIteracoes;
}
public boolean isDuasFases() {
	return duasFases;
}
public void setDuasFases(boolean duasFases) {
	this.duasFases = duasFases;
}

}