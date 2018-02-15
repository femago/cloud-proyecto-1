import { BaseEntity } from './../../shared';

export const enum ApplicationState {
    'IN_PROCESS',
    'CONVERTED'
}

export class Application implements BaseEntity {
    constructor(
        public id?: number,
        public createDate?: any,
        public name?: string,
        public lastname?: string,
        public email?: string,
        public originalRecordContentType?: string,
        public originalRecord?: any,
        public notes?: string,
        public originalRecordLocation?: string,
        public convertedRecordLocation?: string,
        public status?: ApplicationState,
        public contest?: BaseEntity,
    ) {
    }
}
