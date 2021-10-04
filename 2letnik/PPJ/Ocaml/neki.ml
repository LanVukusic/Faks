let vsota_lihih_42 =
  let v = ref 0 in
  let i = ref 0 in
  while !i < 42 do
    v := !v + (2 * !i + 1) ;
    i := !i + 1
  done ;
  !v