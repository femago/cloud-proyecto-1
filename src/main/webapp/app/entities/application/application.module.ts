import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloiceSharedModule } from '../../shared';
import {
    ApplicationService,
    ApplicationPopupService,
    ApplicationComponent,
    ApplicationDetailComponent,
    ApplicationDialogComponent,
    ApplicationPopupComponent,
    ApplicationDeletePopupComponent,
    ApplicationDeleteDialogComponent,
    applicationRoute,
    applicationPopupRoute,
    ApplicationResolvePagingParams,
} from './';
import {ApplicationTableComponent} from './application-table.component';

const ENTITY_STATES = [
    ...applicationRoute,
    ...applicationPopupRoute,
];

@NgModule({
    exports: [
        ApplicationTableComponent
    ],
    imports: [
        CloiceSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ApplicationComponent,
        ApplicationDetailComponent,
        ApplicationDialogComponent,
        ApplicationDeleteDialogComponent,
        ApplicationPopupComponent,
        ApplicationDeletePopupComponent,
        ApplicationTableComponent,
    ],
    entryComponents: [
        ApplicationComponent,
        ApplicationDialogComponent,
        ApplicationPopupComponent,
        ApplicationDeleteDialogComponent,
        ApplicationDeletePopupComponent,
        ApplicationTableComponent,
    ],
    providers: [
        ApplicationService,
        ApplicationPopupService,
        ApplicationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloiceApplicationModule {}
