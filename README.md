- SGBD
- Como funciona a consulta?
- O que é o Criteria
- Por que Criteria?
- Projeto GitHub
- Debugando consultas
- Exemplo Simples
- CriteriaBuilder, CriteriaQuery e From/Root
- Where/Predicates
- Join (Left, Inner, Right, Cross)
- Join Fetch
- Group by
- Having to
- Union?
- Select
- Constructors
- Distinct
- Specification

---

# SGBD - Sistema de Gerenciamento de Banco de Dados

"Um Sistema de Gerenciamento de Banco de Dados (SGBD) – do inglês Data Base Management System (DBMS) – é o conjunto de programas de computador (softwares) responsáveis pelo gerenciamento de uma base de dados. Seu principal objetivo é retirar da aplicação cliente a responsabilidade de gerenciar o acesso, a manipulação e a organização dos dados. O SGBD disponibiliza uma interface para que seus clientes possam incluir, alterar ou consultar dados previamente armazenados. Em bancos de dados relacionais a interface é constituída pelas APIs (Application Programming Interface) ou drivers do SGBD, que executam comandos na linguagem SQL (Structured Query Language)."

Fonte: Wikipédia

Tudo que fazemos em um banco de dados passa pelo SGBD! O SGBD é responsável por tudo, salvar os dados no HD, manter em memória os dados mais acessados, ligar dados e metadados, disponibilizar uma interface para programas e usuários externos acessem o banco de dados (para banco de dados relacionais, é utilizada a linguagem SQL), encriptar dados, controlar o acesso a informações, manter cópias dos dados para recuperação de uma possível falha, garantir transações no banco de dados, enfim, sem o SGBD o banco de dados não funciona!

São basicamente três os componentes de um SGBD:

- Linguagem de definição de dados ou LDD (ou DDL, do inglês), com comandos como CREATE, DROP e ALTER TABLE;
- Linguagem de manipulação de dados, ou LMD (ou DML, do inglês), com comandos como UPDATE, SELECT, INSERT e DELETE;
- Linguagem de controle de dados, ou LCD (ou DCL, do inglês), com comandos para controle de acesso dos usuários do sistema, como GRANT e REVOKE, em SQL.

Os quatro modelos mais conhecidos são:

- hierárquico;
- em rede;
- relacional;
- orientado a objetos.

###### E o objeto-relacional?

O modelo de dados objeto-relacional é praticamente uma mistura do modelo relacional com o orientado a objetos;

# Como funciona a consulta?

### Algebra Relacional

Em ciências da computação, álgebra relacional é uma derivação descendente da lógica de primeira ordem e da álgebra de conjuntos em relação das operações sobre a relação finítimo, que auxilia o trabalho ao identificar os componentes de uma tupla por nome (chamado o atributo) ao invés de uma coluna de chaves numéricas, o qual é chamado a relação na terminologia de banco de dados.

A principal aplicação da álgebra relacional é sustentar a fundamentação teórica de banco de dados relacional, particularmente linguagem de consulta para tais bancos de dados, entre os maiores o SQL.

A álgebra relacional usa conjunto união, conjunto complementar e o produto cartesiano da Teoria dos conjuntos, mas adiciona restrições adicionais a esses operadores.

### SQL Execution Order

uma fonte comum de confusão é o simples fato de que os elementos da sintaxe sql não são ordenados da maneira como são executados. a ordem lexical é:

select [ distinct ] > from > where > group by > having > union > order by

para simplificar, nem todas as cláusulas sql são listadas. esta ordem lexical difere fundamentalmente da ordem lógica (que pode novamente diferir da ordem de execução, dependendo das escolhas do otimizador):

from > where > group by > having > select > distinct > union > order by

1. from é a primeira cláusula, não select. a primeira coisa que acontece é carregar os dados do disco para a memória, para operar esses dados.

1. SELECT é executado após a maioria das outras cláusulas. o mais importante, após FROM e GROUP BY. isso é importante entender quando você pensa que pode fazer referência a coisas que declara na cláusula SELECT da cláusula WHERE. O seguinte não é possível:

```sql
select a.x + a.y as z
from a
where z = 10 -- z is not available here!
```

se quiser reutilizar z, você tem duas opções. ou repita a expressão:

```sql
select a.x + a.y as z
from a
where (a.x + a.y) = 10
```

… Ou você recorre a tabelas derivadas, expressões de tabela comuns ou visualizações para evitar a repetição de código.

3. UNION é colocado antes de ORDER BY na ordenação lexical e lógica. muitas pessoas pensam que cada subseleção de união pode ser ordenada, mas de acordo com o padrão sql e a maioria dos dialetos sql, isso não é verdade. embora alguns dialetos permitam ordenar subconsultas ou tabelas derivadas, não há garantia de que tal ordenação será mantida após uma operação de união

lembre-se sempre da ordem lexical e da ordem lógica das cláusulas sql para evitar erros muito comuns. Se você entender essa distinção, ficará muito óbvio por que algumas coisas funcionam e outras não.

Resumindo, O SQL Query funciona principalmente em três fases:

FROM -> WHERE -> GROUP BY -> HAVING -> SELECT -> DISTINCT -> ORDER BY -> LIMIT.

1. Filtragem de linha - Fase 1: Filtragem de linha - a fase 1 é feita pela cláusula FROM, WHERE, GROUP BY, HAVING.

1. Filtragem de coluna: as colunas são filtradas pela cláusula SELECT.

1. Filtragem de linha - Fase 2: Filtragem de linha - a fase 2 é feita pela cláusula DISTINCT, ORDER BY, LIMIT.

# O que é e Por que usar CRITERIA

A CRITERIA nos permite construir um objeto de consulta de critérios programaticamente, onde podemos aplicar diferentes tipos de regras de filtragem e condições lógicas.

As consultas JPQL são definidas como strings, de maneira semelhante ao SQL. As consultas de critérios JPA, por outro lado, são definidas pela instanciação de objetos Java que representam elementos de consulta.

Por exemplo, construir uma consulta dinâmica com base em campos que um usuário preenche no tempo de execução em um formulário que contém muitos campos opcionais. Espera-se que seja mais limpo ao usar a CRITERIA API JPA, porque elimina a necessidade de construir a consulta usando muitas operações de concatenação de string.

As consultas JPQL baseadas em string e as consultas baseadas em critérios JPA são equivalentes em potência e eficiência. Portanto, escolher um método em vez de outro também é uma questão de preferência pessoal.

A seguinte string de consulta representa uma consulta JPQL mínima:

```sql
SELECT c FROM Country c
```

Uma consulta equivalente pode ser construída usando a API de critérios JPA da seguinte maneira:

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Country> q = cb.createQuery(Country.class);
Root<Country> c = q.from(Country.class);
q.select(c);
```

# Projeto GitHub

### Requisitos

- git
- JDK1.8

Execute o comando para clonar o projeto do github

`git clone https://github.com/mirandarfsm/criteria-tutorial`

Entre na pasta criada e inicie o projeto

```
cd criteria-tutorial/
./mvnw
```

Abra o arquivo `src/test/java/com/mycompany/myapp/MainIT.java` para edição

Execute o teste `assertThatActorsTablesIsNotNull` e verifique se passou tudo certo!

```
./mvnw -P-webpack test -Dtest=MainIT#assertThatActorsTablesIsNotNull
```

# Debugando consultas

Abra o arquivo `src/test/resources/config/application.yml`

Procure o argumento `show-sql` e altere para `true`

```yml
jpa:
  database-platform: io.github.jhipster.domain.util.FixedH2Dialect
  open-in-view: false
  show-sql: true
```

Execute o teste `assertThatActorsTablesIsNotNull` e verifique a consulta no console

```
2021-08-05 15:42:33.425 DEBUG 13089 --- [           main] c.e.c.E.m.myapp.domain.Authority         : Initialize successful.
2021-08-05 15:42:33.428 DEBUG 13089 --- [           main] c.e.c.E.m.myapp.domain.User.authorities  : Initialize successful.
2021-08-05 15:42:33.431 DEBUG 13089 --- [           main] c.e.c.E.mycompany.myapp.domain.Movie     : Initialize successful.
2021-08-05 15:42:33.433 DEBUG 13089 --- [           main] c.e.c.E.mycompany.myapp.domain.Actor     : Initialize successful.
2021-08-05 15:42:33.436 DEBUG 13089 --- [           main] c.e.c.E.m.myapp.domain.Actor.movies      : Initialize successful.
2021-08-05 15:42:36.775 DEBUG 13089 --- [           main] i.m.c.u.i.logging.InternalLoggerFactory  : Using SLF4J as the default logging framework
2021-08-05 15:42:38.548  INFO 13089 --- [           main] com.mycompany.myapp.MainIT               : Started MainIT in 7.056 seconds (JVM running for 7.938)
Hibernate: select actor0_.id as id1_0_, actor0_.birthdate as birthdat2_0_, actor0_.name as name3_0_ from actor actor0_
2021-08-05 15:42:38.766 DEBUG 13089 --- [extShutdownHook] c.ehcache.core.Ehcache-usersByEmail      : Close successful.
2021-08-05 15:42:38.767 DEBUG 13089 --- [extShutdownHook] c.e.c.E.mycompany.myapp.domain.User      : Close successful.
2021-08-05 15:42:38.768 DEBUG 13089 --- [extShutdownHook] c.e.c.E.mycompany.myapp.domain.Movie     : Close successful.
2021-08-05 15:42:38.768 DEBUG 13089 --- [extShutdownHook] c.e.c.E.mycompany.myapp.domain.Actor     : Close successful.
2021-08-05 15:42:38.769 DEBUG 13089 --- [extShutdownHook] c.ehcache.core.Ehcache-usersByLogin      : Close successful.
2021-08-05 15:42:38.769 DEBUG 13089 --- [extShutdownHook] c.e.c.E.m.myapp.domain.Actor.movies      : Close successful.
2021-08-05 15:42:38.769 DEBUG 13089 --- [extShutdownHook] c.e.c.E.m.myapp.domain.Authority         : Close successful.
2021-08-05 15:42:38.769 DEBUG 13089 --- [extShutdownHook] c.e.c.E.m.myapp.domain.User.authorities  : Close successful.

```

# Exemplo Simples

Vamos começar examinando como recuperar dados usando consultas CRITERIA. Veremos como obter todas as instâncias de uma classe específica do banco de dados.

Temos uma classe Actor que representa a tupla “actor” no banco de dados:

```java
public class Actor implements Serializable {
  private Long id;
  private String name;
  private Instant birthdate;
  private Set<Movie> movies;
  // standard setters and getters
}
```

Vejamos uma consulta de critérios simples que recuperará todas as linhas de “Actor” do banco de dados:

```java
CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
Root<Actor> root = criteriaQuery.from(Actor.class);
criteriaQuery.select(root);

TypedQuery<Actor> query = em.createQuery(criteriaQuery);
List<Actor> results = query.getResultList();
```

# CriteriaBuilder, CriteriaQuery e From/Root

A interface `CriteriaBuilder` serve como a principal fábrica de consultas de critérios e elementos de consulta de critérios. `CriteriaBuilder` define API para criar objetos `CriteriaQuery` e define todas as operações de comparação com suporte e funções usadas para definir as cláusulas da consulta.

`CriteriaQuery` define uma consulta de seleção de banco de dados. Um `CriteriaQuery` modela todas as cláusulas de uma consulta de seleção JPQL. Os elementos de um `CriteriaQuery` não podem ser usados ​​em outros `CriteriaQuerys`. Um `CriteriaQuery` é usado com a API EntityManager `createQuery()` para criar uma consulta JPA.

`CriteriaQuery` define as seguintes cláusulas e opções:

- `distinct`(boolean) - Define se a consulta deve filtrar resultados duplicados (o padrão é falso). Se for usada uma junção com um relacionamento de coleção, deve ser usado `distinct` para evitar resultados duplicados.
- `from`(Class) - Define e retorna um elemento na cláusula from da consulta para a classe de entidade. É necessário pelo menos um elemento from para que a consulta seja válida.
- `from`(EntityType) - Define e retorna um elemento na cláusula from da consulta para o tipo de entidade de metamodelo. É necessário pelo menos um elemento from para que a consulta seja válida.
- `select`(Selection) - Define a cláusula select da consulta. Se não for definido, a primeira raiz será selecionada por padrão.
- `multiselect`(Selection...), multiselect(List<Selection>) - Define uma consulta de seleção múltipla.
- `where`(Expression), where(Predicate...) - Define a cláusula where da consulta. Por padrão, todas as instâncias da classe são selecionadas.
- `orderBy`(Order...), orderBy(List<Order>) - Define a cláusula de pedido da consulta. Por padrão, os resultados não são ordenados.
- `groupBy`(Expression...), groupBy(List<Expression>) - Define a cláusula group by da consulta. Por padrão, os resultados não são agrupados.
- `having`(Expression), having(Predicate...) - Define a cláusula `having` da consulta. `Having` permite que resultados agrupados sejam filtrados.
- `subQuery`(Class) - Cria uma subQuery para ser usada em uma das outras cláusulas.

A cláusula `from` define o que está sendo consultado. A cláusula `from` é definida usando a API `from` em `CriteriaQuery`. Um objeto Root é o retorno de `from`, que representa o objeto no contexto da consulta.

# Where/Predicates

A classe Predicate faz parte da API Criteria e é usada para construir cláusulas where.

- `equal`
- `in`
- `between`
- `like`
- `gt`
- `ge`
- `lt`
- `le`
- `isEmpty`
- `isFalse`
- `isTrue`
- `isNull`

```java
Predicate idEqualZeroPredicate = criteriaBuilder.equal(root.get("id"), 0);
```

# Join (Left, Inner, Right, Cross)

Existem 5 tipos de Join's, são eles:

- cross join
- equi join
- semi join
- anti join
- division

Esses termos são comumente usados ​​em álgebra relacional. No SQL usa-se termos diferentes para os conceitos acima, sendo que alguns nem existem. vamos dar uma olhada neles:

### cross join

Esse produz um produto cruzado das duas referências de tabela unidas, combinando cada registro da primeira referência de tabela com cada registro da segunda referência de tabela. Isso pode ser alcançado com referências a tabelas separadas por vírgulas na cláusula from. Nos raros casos em que é realmente necessário, você pode escrever uma junção cruzada explicitamente.

![image](https://user-images.githubusercontent.com/6695037/129268044-e06cffd0-bda1-48d9-8f51-128d0e5b3485.png)

### equi join

Esta é a operação de junção mais comum. Tem dois sub-niveis:

- inner join (or just join)
- outer join (as left, right, full outer join)

![image](https://user-images.githubusercontent.com/6695037/129268019-0a1e846f-4a01-403f-8e73-0d24c14f94e0.png)

### semi join

Este conceito relacional pode ser expresso de duas maneiras em sql: usando um predicado in ou usando um predicado exists. “Semi” significa “metade” em latim. Este tipo de junção é usado para juntar apenas “metade” de uma referência de tabela.

![image](https://user-images.githubusercontent.com/6695037/129267986-5468f8a8-0ac9-4e9d-83a7-397298deb59c.png)
  
embora não haja uma regra geral sobre se você deve preferir in ou exists, podemos concluir que:

- predicados in tendem a ser mais legíveis do que os predicados existents
- predicados existents tendem a ser mais expressivos do que predicados in (ou seja, é mais fácil expressar semi-junção muito complexa)
- não há diferença formal no desempenho. pode, entretanto, haver uma grande diferença de desempenho em alguns bancos de dados.

### anti join

Este conceito relacional é exatamente o oposto de um semi-join. Você pode produzi-lo simplesmente adicionando uma palavra-chave not aos predicados in ou exists.

![image](https://user-images.githubusercontent.com/6695037/129267964-ca754f15-f20c-46e3-877f-9dd1ed00cfcc.png)
  
Aplicam-se as mesmas regras em relação ao desempenho, legibilidade e expressividade

### division

A division é realmente um bicho de sete cabeças. Em resumo, se cross join é multiplicação, division é o inverso. divisão é o inverso de uma operação de junção cruzada division são muito difíceis de expressar em sql.

![image](https://user-images.githubusercontent.com/6695037/129267920-94c717aa-7903-4703-b696-ee6e815eb444.png)

## Join Fetch

A operação de Fetch pode ser usada em um Join para buscar os objetos relacionados em uma única consulta. Isso evita consultas adicionais para cada um dos relacionamentos do objeto e garante que os relacionamentos LAZY sejam carregados.

# Group by

A cláusula Group By permite que informações resumidas sejam calculadas em um conjunto de objetos. Group By é normalmente usado em conjunto com funções de agregação.

### Aggregation functions

As funções de agregação podem incluir informações resumidas sobre um conjunto de objetos. Essas funções podem ser usadas para retornar um único resultado ou podem ser usadas com um groupBy para retornar vários resultados.

As funções agregadas são definidas no CriteriaBuilder e incluem:

- `max(Expression)` - Retorna o valor máximo para todos os resultados. Usado para tipos numéricos.
- `greatest(Expression)` - Retorna o valor máximo para todos os resultados. Usado para tipos não numéricos.
- `min(Expression)` - Retorna o valor mínimo para todos os resultados. Usado para tipos numéricos.
- `least(Expression)` - Retorna o valor mínimo para todos os resultados. Usado para tipos não numéricos.
- `avg(Expression)` - Retorna a média média de todos os resultados. Um Double é retornado.
- `sum(Expression)` - Retorna a soma de todos os resultados.
- `sumAsLong(Expression)` - Retorna a soma de todos os resultados. Um Long é retornado.
- `sumAsDouble(Expression)` - Retorna a soma de todos os resultados. Um Double é retornado.
- `count(Expression)` - Retorna a contagem de todos os resultados. valores nulos não são contados. Um Long é retornado.
- `countDistinct(Expression)` - Retorna a contagem de todos os resultados distintos. valores nulos não são contados. Um Long é retornado.

# Having to

A cláusula Having to permite que os resultados de um Group by sejam filtrados. A cláusula having to é definida usando os Predicate's igual no Where.

# Union?

O JPQL oferece suporte a um conjunto de recursos que permite criar consultas até uma certa complexidade. Essas consultas são boas o suficiente para a maioria dos casos de uso. Mas se quiser implementar consultas utilizando SQL mais avançado, não terá suporte, tendo que utilizar consultas nativas.

Alguns exemplos:

1. Usar SubQueries fora das cláusulas WHERE e HAVING TO
1. Executar operações definidas (UNION, INTERSECT and EXCEPT)
1. Escrever queries recursivas

# Select

Select define o que é selecionado por uma consulta. Um Select pode ser qualquer expressão de objeto, expressão de atributo, função, sub-seleção, construtor ou função de agregação. Um alias pode ser definido para uma Seleção usando `alias()`.

## Constructors

O Constructor pode ser usado com uma classe e valores para retornar objetos de dados de uma consulta de critérios. Esses não serão objetos gerenciados e a classe deve definir um construtor que corresponda aos argumentos e tipos. As consultas do construtor podem ser usadas para selecionar dados parciais ou dados de relatório sobre objetos e obter de volta uma instância de classe em vez de uma matriz de objeto ou tupla.

## Distinct

Define se a consulta deve filtrar resultados duplicados (o padrão é falso).

# Specification

Para ser capaz de definir Predicate's reutilizáveis, o Spring Data introduziu a interface de Specification que é derivada de conceitos introduzidos no livro Domain Driven Design de Eric Evans. Ele define uma especificação como um predicado sobre uma entidade que é exatamente o que nossa interface de specification representa. Na verdade, consiste apenas em um único método:

```java
public interface Specification<T> {
  Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb);
}
```

### Exemplos

```java
private Specification<Actor> createProjection() {
  return (root, query, build) -> {
      query.distinct(true);
      root.fetch(Actor_.movies, JoinType.LEFT);
      return null;
  };
}
```

```java
private Specification<Actor> filterByIdIn(List<Long> ids) {
  return (root, query, builder) -> root.get(Actor_.id).in(ids);
}
```

```java
private Specification<Movie> filterByYearGreaterThanToday() {
  return (root, query, build) -> {
      return build.greaterThan(root.get(Movie_.year), 1980);
  };
}
```

```java
private Specification<Actor> filterByRoles() {
  return (root, query, builder) -> {
      List<String> movies = Arrays.asList("Beat Street", "Power");
      Subquery<Movie> movieSubquery = query.subquery(Movie.class);
      Root<Movie> movie = movieSubquery.from(Movie.class);
      Join<Movie, Actor> join = movie.join(Movie_.actors, JoinType.LEFT);
      Predicate equal = builder.equal(join.get(Actor_.id), root.get(Actor_.id));
      Predicate in = movie.get(Movie_.title).in(movies);
      Predicate where = builder.and(equal, in);
      movieSubquery.select(movie).distinct(true).where(where);
      return builder.exists(movieSubquery);
  };
}
```

# Referência

[O que é um SGBD?](https://dicasdeprogramacao.com.br/o-que-e-um-sgbd/)

[Sistema de gerenciamento de banco de dados](https://pt.wikipedia.org/wiki/Sistema_de_gerenciamento_de_banco_de_dados)

[Relational algebra](https://en.wikipedia.org/wiki/Relational_algebra)

[10 Easy Steps to a Complete Understanding of SQL
](https://dzone.com/articles/10-easy-steps-to-a-complete-understanding-of-sql)

[How does a SQL query work?](https://stackoverflow.com/a/67148174)

[Java e JPA: Consultas avançadas, performance e modelos complexos](https://www.alura.com.br/curso-online-java-jpa-consultas-avancadas-performance-modelos-complexos?gclid=CjwKCAjw0qOIBhBhEiwAyvVcf-kEeibKWAp8IB96fPD7BOsRBYwBDONEgBqti-kkJG0knNxvEW-VcxoCybMQAvD_BwE)

[JPA Criteria API Queries
](https://www.objectdb.com/java/jpa/query/criteria)

[What is the difference between a criteria, a predicate, and a specification?](https://stackoverflow.com/a/47604610)

[Java Persistence/Criteria](https://en.wikibooks.org/wiki/Java_Persistence/Criteria)

[Combining JPA And/Or Criteria Predicates](https://www.baeldung.com/jpa-and-or-criteria-predicates)

[JPA Criteria Queries](https://www.baeldung.com/hibernate-criteria-queries)

[Use Criteria Queries in a Spring Data Application](https://www.baeldung.com/spring-data-criteria-queries)

[Chapter 14. HQL: The Hibernate Query Language](https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html)

[Chapter 16. Criteria Queries](https://docs.jboss.org/hibernate/orm/3.5/reference/en/html/querycriteria.html)

[Advanced Spring Data JPA - Specifications and Querydsl](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/)

[10. Thinking that 50ms is fast query execution](https://blog.jooq.org/2013/08/12/10-more-common-mistakes-java-developers-make-when-writing-sql/)

[5. Not knowing about the N+1 problem](https://blog.jooq.org/2014/05/26/yet-another-10-common-mistakes-java-developer-make-when-writing-sql-you-wont-believe-the-last-one/)

[Best way to understand complex SQL statements?](https://stackoverflow.com/questions/379062/best-way-to-understand-complex-sql-statements)

[Semi Join and Anti Join Should Have Their Own Syntax in SQL](https://blog.jooq.org/2015/10/13/semi-join-and-anti-join-should-have-its-own-syntax-in-sql/)

[A comprehensive interpretation of PostgreSQL and Greenplum’s hash join](https://developpaper.com/a-comprehensive-interpretation-of-postgresql-and-greenplums-hash-join/)

[Advanced SQL: Relational division in jOOQ](https://blog.jooq.org/2012/03/30/advanced-sql-relational-division-in-jooq/)

[PostgreSQL and relational algebra (Equi-Join, Semi-Join, Anti-Join, Division)](https://www.programmersought.com/article/24551080339/)

[10.2. JPQL Language Reference](https://docs.oracle.com/html/E13946_04/ejb3_langref.html)

[Is your query too complex for JPA and Hibernate?](https://thorben-janssen.com/query-complex-jpa-hibernate/)

[FetchMode in Hibernate](https://www.baeldung.com/hibernate-fetchmode)
