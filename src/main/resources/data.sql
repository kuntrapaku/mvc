insert into accounts (id, customer_name, balance, version)
values (1, 'Surendra', 1000, 0)
on conflict (id) do nothing;

insert into accounts (id, customer_name, balance, version)
values (2, 'Divya', 500, 0)
on conflict (id) do nothing;