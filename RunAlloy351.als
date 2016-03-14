
abstract sig Boolean {}
one sig true extends Boolean {}
abstract sig Var { v : lone Boolean }
one sig a, b extends Var {}
pred l1[] { (  (some b.v)  and  (some a.v)  ) }
pred l2[] { (  (some b.v)  and  (some a.v)  ) }
assert equivalent {
    l1 <=> l2
}
check equivalent
