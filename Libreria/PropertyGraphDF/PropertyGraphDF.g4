grammar PropertyGraphDF;
file
    :   nf #NodeFile
    |   ff #FullFile
    ;
nf: nodeProp (declaration NL)* ;
ff: standardProps (declaration NL)* ;
nodeProp: '@id;@label;' newPropN* NL ;
standardProps: '@id;@label;' newPropN* '@undirected;@source;@target;' newPropE* NL;
//edgeProp: '@id;@undirected;@label;@source;@target;' newProp* NL ;
newPropN: PROPNAME';';
newPropE: PROPNAME';';
declaration: STRING ';' label ';' (value ';')* ;
//nodeDeclaration: STRING ';' label ';' (value ';')* ;
//edgeDeclaration: STRING ';' label ';' (';'+)  booleanValue ';' STRING ';' STRING ';' (value ';')* ;
label: '[' STRING (',' STRING)* ']' | '[]';
value
    :   STRING      #String
    |   booleanValue     #Boolean
    |   NUMBER      #Number
    |   nullValue       #Null
    |   list    #ListValue
    |            #Nothing
    ;
list: '[' STRING (',' STRING)* ']' | '[]';
PROPNAME: [a-zA-Z_-]+ ;
STRING:   '"' (.)*? '"' ;
booleanValue: ('T' | 'F') ;
nullValue: 'N' ;
NUMBER: INT | FLOAT ;
INT:    DIGIT+ ;
FLOAT:  DIGIT+ '.' DIGIT* ;
fragment DIGIT: [0-9] ;
NL: '\r'? '\n' ;