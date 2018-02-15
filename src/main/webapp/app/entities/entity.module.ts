import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CloiceContestModule } from './contest/contest.module';
import { CloiceApplicationModule } from './application/application.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CloiceContestModule,
        CloiceApplicationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloiceEntityModule {}
