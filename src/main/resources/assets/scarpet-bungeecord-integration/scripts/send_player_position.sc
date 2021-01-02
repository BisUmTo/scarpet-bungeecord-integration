global_nbt_list = ['Rotation', 'Pos'];
looper() -> (
    for(player('*'),
        send_packet(_, 'multicore:redcraft/position', _~'x', _~'y', _~'z', _~'pitch', _~'yaw');
    );
    schedule(1800, 'looper')
);
looper()
