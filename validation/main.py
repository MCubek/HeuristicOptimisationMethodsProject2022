import subprocess
import re
import itertools
import pandas as pd
import ast
import matplotlib.pyplot as plt
from multiprocessing import Pool

def call_program(program_name = 'solution.jar', input_file = 'resources/i1[2].TXT', output_file = 'outputs/o1.txt', minutes = 5):
    print(['java', '-jar', program_name, input_file, output_file, str(minutes)])
    process = subprocess.Popen(['java', '-jar', program_name, input_file, output_file, str(minutes)], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

    ret = []
    while process.poll() is None:
        line = process.stdout.readline()
        if line != '' and line.endswith('\n'):
            ret.append(line[:-1])
    stdout, stderr = process.communicate()
    ret += stdout.split('\n')
    if stderr != '':
        ret += stderr.split('\n')
    ret.remove('')
    return ret


def parse_results(ret):
    results = ret.get().__str__()
    data = pd.DataFrame(columns = ['Iteration', 'Vehicles', 'Time', 'Score'])

    i = 0
    for row in ast.literal_eval(results):
        if 'Iteration' in row:
            row_results = row.split(',')
            iteration = int(row_results[0].split(' ')[-1])
            vehicles = int(row_results[1].split(' ')[-1])
            time = int(row_results[2].split(' ')[-1])
            score = float(row_results[3].split(' ')[-1])
            data.loc[i] = (iteration, vehicles, time, score)
            i += 1

    return data

def graph_instance(data, label, length, main_column = 'Iteration', columns = ['Vehicles', 'Time', 'Score']):
    print("Starting task for example " + str(label) + ", duration: " + str(length))

    for c in columns:
        plt.figure(figsize=(8, 6), dpi=80)
        plt.title(label)
        plt.ylabel(c)
        plt.xlabel("Iteration")
        x = data[main_column] + 1
        y = data[c]
        plt.plot(x, y, label=f'{label}, best:{data.iloc[-1][c]}')
        plt.legend()

        plt.savefig(f'images/{label}_{length}_{c.lower()}.png', bbox_inches='tight')
        # plt.show()
        print("Finished task for example " + str(label) + ", duration: " + str(length))



if __name__ == '__main__':

    pool = Pool()
    results = []

    inputs = ['resources/i1[2].TXT', 'resources/i2[5].TXT', 'resources/i3[4].TXT', 'resources/i4[4].TXT', 'resources/i5[4].TXT']
    outputs = ['outputs/o1[2].TXT', 'outputs/o2[5].TXT', 'outputs/o3[4].TXT', 'outputs/o4[4].TXT', 'outputs/o5[4].TXT']
    minutes = [1, 5, 60*6]

    # print(f'Starting task')
    # results.append(pool.apply_async(call_program, args=('solution.jar', 'resources/i1[2].TXT', 'outputs/o11.txt', 1)))
    # results.append(pool.apply_async(call_program, args=('solution.jar', 'resources/i2[5].TXT', 'outputs/o22.txt', 1)))

    for minute in minutes:
        for i, input in enumerate(inputs):
            output = outputs[i].split('.')[0] + '_' + str(minute) + '.' + outputs[i].split('.')[1]
            results.append(pool.apply_async(call_program, args=('solution.jar', input, output, minute)))

    j = 0
    for minute in minutes:
        for i, input in enumerate(inputs):
            print(input.split('/')[-1].split('.')[0])
            result = results[j]
            result.get()
            graph_instance(parse_results(result), input.split('/')[-1].split('.')[0], minute)
            j += 1

    # for result in results:
    #     ret = result
    #     result.get()
    #     graph_instance(parse_results(result), '1', 1)

    print('Finished all tasks')

