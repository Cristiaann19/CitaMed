import { TestBed } from '@angular/core/testing';

import { HorarioMedicoService } from './horario-medico-service';

describe('HorarioMedicoService', () => {
  let service: HorarioMedicoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HorarioMedicoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
